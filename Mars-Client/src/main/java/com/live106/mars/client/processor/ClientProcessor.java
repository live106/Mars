package com.live106.mars.client.processor;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.live106.mars.client.ClientRunner;
import com.live106.mars.client.console.ConsoleHandler;
import com.live106.mars.client.service.ClientService;
import com.live106.mars.protocol.handler.ProcessorListener;
import com.live106.mars.protocol.handler.ProtocolProcessor;
import com.live106.mars.protocol.handler.annotation.Processor;
import com.live106.mars.protocol.handler.annotation.ProcessorMethod;
import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.thrift.LoginCode;
import com.live106.mars.protocol.thrift.LoginType;
import com.live106.mars.protocol.thrift.Notify;
import com.live106.mars.protocol.thrift.PeerType;
import com.live106.mars.protocol.thrift.RequestLoadScenario;
import com.live106.mars.protocol.thrift.RequestSendClientPublicKey;
import com.live106.mars.protocol.thrift.RequestStoreScenario;
import com.live106.mars.protocol.thrift.RequestUserLogin;
import com.live106.mars.protocol.thrift.ResponseAuthServerPublicKey;
import com.live106.mars.protocol.thrift.ResponseGameConnect;
import com.live106.mars.protocol.thrift.ResponseLoadArchive;
import com.live106.mars.protocol.thrift.ResponseSaveArchive;
import com.live106.mars.protocol.thrift.ResponseSendClientPublicKey;
import com.live106.mars.protocol.thrift.ResponseStoreScenario;
import com.live106.mars.protocol.thrift.ResponseUserLogin;
import com.live106.mars.protocol.thrift.ReuqestGameConnect;
import com.live106.mars.protocol.thrift.ScenarioInfo;
import com.live106.mars.protocol.thrift.TaskInfo;
import com.live106.mars.protocol.util.Cryptor;
import com.live106.mars.protocol.util.ProtocolCrypto;
import com.live106.mars.protocol.util.ProtocolSerializer;
import com.live106.mars.util.LoggerHelper;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Service
@Processor
@Scope(value = "prototype")
public class ClientProcessor implements ProtocolProcessor {

	private final static Logger logger = LoggerFactory.getLogger(ClientProcessor.class);

	@Autowired
	private ClientService clientService;
	private byte[] serverPubKey;
	@SuppressWarnings("unused")
	private byte[] secretKey;
	private Map<String, Object> keyMap;

	private Cryptor cryptor = new Cryptor(Cryptor.AES);
	private AtomicInteger sequeceId = new AtomicInteger(1);

	private ClientRunner runner;

	@ProcessorMethod(messageClass = ResponseAuthServerPublicKey.class)
	public ProtocolBase receiveServerPublicKey(ChannelHandlerContext context, ResponseAuthServerPublicKey response)
			throws TException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			IllegalStateException {
		if (keyMap == null || keyMap.size() == 0) {
			keyMap = ProtocolCrypto.generateKey();
		}
		String serverKeyStr = response.getServerPubKey();
		this.serverPubKey = Base64.getDecoder().decode(serverKeyStr);
		secretKey = ProtocolCrypto.getSecretKey(this.serverPubKey, ProtocolCrypto.getPrivateKey(keyMap));

		RequestSendClientPublicKey request = new RequestSendClientPublicKey();
		request.setClientPubKey(Base64.getEncoder().encodeToString(ProtocolCrypto.getPublicKey(keyMap)));

		return ProtocolSerializer.serialize(request, new ProtocolPeer2Peer());
	}

	@ProcessorMethod(messageClass = ResponseSendClientPublicKey.class)
	public ProtocolBase receiveSendClientPublicKey(ChannelHandlerContext context, ResponseSendClientPublicKey response)
			throws Exception {
		boolean result = response.isResult();
		if (result) {
			RequestUserLogin request = new RequestUserLogin();
			cryptor.setSecretKey(runner.getSecretKey());
			String machineId = runner.getMachineId();
			request.setType(LoginType.USER_GUEST);
			
			if (StringUtils.isNotBlank(machineId)) {
				request.setMachineId(
					Base64.getEncoder().encodeToString((cryptor.doCrypt(machineId.getBytes(), Cipher.ENCRYPT_MODE))));
			}

			return ProtocolSerializer.serialize(request, new ProtocolPeer2Peer());
		} else {
			logger.error(String.format("server receive %s public key failed!", runner.toString()));
			return null;
		}

	}

	@ProcessorMethod(messageClass = ResponseUserLogin.class)
	public ProtocolBase receiveUserLogin(ChannelHandlerContext context, ResponseUserLogin response) throws TException {
		LoginCode code = response.getCode();
		switch (code) {
		case OK:
			break;
		case ERROR:
			break;
		default:
			break;
		}

		String msg = response.getMsg();
		// String uid = response.getUid();
		// String secureKey = response.g`etSecureKey();
		// String gameserver = response.getGameserver();

		clientService.setLoginResponse(response);

		String machineId = response.getMachineId();
		
		if (StringUtils.isNotEmpty(runner.getMachineId()) && !runner.getMachineId().equals(machineId)) {
			LoggerHelper.warn(logger, ()->String.format("与服务器返回唯一码不匹配{%s}-->{%s}", runner.getMachineId(), machineId));
		}
		
		int uid = response.getUid();
		String passport = response.getPassport();

		runner.setMachineId(machineId);
		runner.setUid(uid);
		runner.setPassport(passport);

		logger.info("{}-->{}-->{} login {}, {}", machineId, uid, passport, code.name(), msg);

		ReuqestGameConnect request = new ReuqestGameConnect();

		ProtocolPeer2Peer protocol = new ProtocolPeer2Peer();
		protocol.getHeader().setTargetType(PeerType.PEER_TYPE_GAME);
		// protocol.setTargetType(PeerType.PEER_TYPE_GAME);
		// protocol.setTargetId(gameserver);

		request.setPassport(passport);

		request.setUid(uid);
		request.setGameserver("");
		request.setSequenceId(sequeceId.getAndIncrement());

		String checkString = String.format("%s%d%d", request.getGameserver(), request.getUid(),
				request.getSequenceId());
		Cryptor cryptor = new Cryptor(Cryptor.AES);
		cryptor.setSecretKey(response.getSecureKey());
		try {
			byte[] encryptData = cryptor.doCrypt(checkString.getBytes(), Cipher.ENCRYPT_MODE);
			request.setRandomKey(Base64.getEncoder().encodeToString(encryptData));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		return ProtocolSerializer.serialize(request, protocol);
	}

	@ProcessorMethod(messageClass = ResponseGameConnect.class)
	public ProtocolBase receiveGameConnect(ChannelHandlerContext context, ResponseGameConnect response)
			throws TException {
		LoggerHelper.debug(logger, () -> String.format("玩家{%d}登录游戏服务器：%s", runner.getUid(), response.isResult()));

		// 存储关卡信息
		RequestStoreScenario request = new RequestStoreScenario();

		ProtocolPeer2Peer protocol = new ProtocolPeer2Peer();
		protocol.getHeader().setTargetType(PeerType.PEER_TYPE_GAME);
		protocol.getHeader().setSourceId(runner.getUid());
		protocol.getHeader().setPassport(runner.getPassport());

		for (int i = 0; i < 5; i++) {
			ScenarioInfo scenario = new ScenarioInfo();
			scenario.setId(i + 1);
			Map<Integer, TaskInfo> tasks = new HashMap<>();
			for (int j = 0; j < 3; j++) {
				TaskInfo task = new TaskInfo();
				task.setId(j + 1);
				task.setValue((j + 1) * 100);
				task.setTime(System.currentTimeMillis());
				tasks.put(task.getId(), task);
			}
			scenario.setTasks(tasks);

			request.addToScenarios(scenario);
		}

		return ProtocolSerializer.serialize(request, protocol);
	}

	@ProcessorMethod(messageClass = ResponseStoreScenario.class)
	public ProtocolBase receiveStoreScenario(ChannelHandlerContext context, ResponseStoreScenario response)
			throws TException {
		LoggerHelper.debug(logger, () -> String.format("玩家{%d}存储场景信息：%s", runner.getUid(), response.isResult()));

		// 读取关卡信息
		RequestLoadScenario request = new RequestLoadScenario();

		ProtocolPeer2Peer protocol = new ProtocolPeer2Peer();
		protocol.getHeader().setTargetType(PeerType.PEER_TYPE_GAME);
		protocol.getHeader().setSourceId(runner.getUid());
		protocol.getHeader().setPassport(runner.getPassport());

		request.setId(2);

		return ProtocolSerializer.serialize(request, protocol);
	}

	@ProcessorMethod(messageClass = ScenarioInfo.class)
	public ProtocolBase receiveLoadScenario(ChannelHandlerContext context, ScenarioInfo response) throws TException {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("玩家{%d}读取场景信息", runner.getUid()));
		sb.append("\n");
		sb.append(response.getId());
		sb.append("\n");
		sb.append("---------------------");
		Map<Integer, TaskInfo> tasks = response.getTasks();
		if (tasks != null) {
			for (TaskInfo task : tasks.values()) {
				sb.append("\n");
				sb.append(task.getId());
				sb.append(":");
				sb.append(task.getValue());
				sb.append(":");
				sb.append(task.getTime());
			}
		}
		sb.append("\n");
		sb.append("---------------------");

		LoggerHelper.debug(logger, () -> sb.toString());
		
		//从登陆开始重新循环整个请求流程
		if (ConsoleHandler.loopRequest) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);//
						runner.reconnect();
						runner.start();
					} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InterruptedException | TException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		return null;
	}
	
	@ProcessorMethod(messageClass = ResponseSaveArchive.class)
	public ProtocolBase receiveSaveArchive(ChannelHandlerContext context, ResponseSaveArchive response) throws TException {
		LoggerHelper.debug(logger, ()->String.format("save archive result : %s %s", response.isResult(), response.getMsg()));
		return null;
	}
	
	@ProcessorMethod(messageClass = ResponseLoadArchive.class)
	public ProtocolBase receiveLoadArchive(ChannelHandlerContext context, ResponseLoadArchive response) throws TException {
		
		LoggerHelper.debug(logger, ()->String.format("load archive result : %s %s", response.isResult(), response.getMsg()));
		
		if (response.isResult()) {
			LoggerHelper.debug(logger, () -> {
				try {
					return String.format("%s load archive data : \n%s", runner.toString(), response.getArchive().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return String.format("%s parse load archive data error.", runner.toString());
			});
		}
		return null;
	}

	@ProcessorMethod(messageClass = Notify.class)
	public ProtocolBase receiveNotify(ChannelHandlerContext context, Notify notify) throws TException {
		LoggerHelper.debug(logger, () -> notify.getMessage());
		return null;
	}

	public void setServerService(ClientService serverService) {
		this.clientService = serverService;
	}

	@Override
	public void setListener(ProcessorListener listener) {
		this.runner = (ClientRunner) listener;
	}

	@Override
	public ProcessorListener getListener() {
		return this.runner;
	}

}
