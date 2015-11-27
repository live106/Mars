/**
 * 
 */
package com.live106.mars.account.rpc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.account.bean.UserPassport;
import com.live106.mars.account.redis.IRedisConstants;
import com.live106.mars.account.service.UserService;
import com.live106.mars.protocol.thrift.IUserService.Iface;
import com.live106.mars.protocol.thrift.LoginCode;
import com.live106.mars.protocol.thrift.LoginType;
import com.live106.mars.protocol.thrift.Notify;
import com.live106.mars.protocol.thrift.RequestAuthServerPublicKey;
import com.live106.mars.protocol.thrift.RequestSendClientPublicKey;
import com.live106.mars.protocol.thrift.RequestUserLogin;
import com.live106.mars.protocol.thrift.ResponseAuthServerPublicKey;
import com.live106.mars.protocol.thrift.ResponseSendClientPublicKey;
import com.live106.mars.protocol.thrift.ResponseUserLogin;
import com.live106.mars.protocol.thrift.game.MessageUserSecureInfo;
import com.live106.mars.protocol.util.ProtocolSerializer;
import com.live106.mars.util.LoggerHelper;

/**
 * 用户相关RPC服务
 * @author live106 @creation Oct 16, 2015
 */
@Service
public class UserServiceRpc implements Iface, IRedisConstants {
	
	private final static Logger logger = LoggerFactory.getLogger(UserServiceRpc.class);
	
	@Autowired
	private UserService userService;

	@Override
	public String ping(String visitor) throws TException {
		return "hello " + visitor;
	}

	/**
	 * 获取服务器DH公钥
	 */
	@Override
	public ResponseAuthServerPublicKey getPubKey(RequestAuthServerPublicKey request, String channelId) throws TException {
		ResponseAuthServerPublicKey resp = new ResponseAuthServerPublicKey();
		try {
			resp.setServerPubKey(userService.getServerPublicKeyBase64());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return resp;
	}

	/**
	 * 保存客户端发来的公钥
	 */
	@Override
	public ResponseSendClientPublicKey sendPubKey(RequestSendClientPublicKey request, String channelId) throws TException {
		boolean result = false;
		String clientPubKey = request.getClientPubKey();
		ResponseSendClientPublicKey resp = new ResponseSendClientPublicKey();
		
		if (StringUtils.isBlank(clientPubKey)) {
			resp.setResult(false);
			resp.setMsg("client public key is invalid." + clientPubKey);
			return resp;
		}
		userService.storeClientAesKey(channelId, clientPubKey);
		
		LoggerHelper.debug(logger, ()->String.format("received client public key %s-->%s",  channelId, Arrays.toString(clientPubKey.getBytes())));
		
		result = true;
		resp.setResult(result);
		resp.setMsg("server has received client public key.");
		return resp;
	}
	
	/**
	 * 账号登录处理 //Redis version
	 */
	@Override
	public ResponseUserLogin doLogin(RequestUserLogin request, String channelId) throws TException {
		Map<String, String> usermap = null;
		
		String json = ProtocolSerializer.serializeJson(request);
		LoggerHelper.debug(logger, ()->String.format("收到登录请求：\n%s", json));
		
		ResponseUserLogin resp = new ResponseUserLogin();
		try {
			LoginType type = request.getType();
			switch (type) {
			case USER_ACCOUNT:
			{
				throw new Notify("User login type not supported!" + type.name());
			}
//			{
//				String encrytedUsername = request.getUsername();
//				String encrytedPassword = request.getPassword();
//				
//				if (encrytedUsername == null || encrytedPassword == null) {
//					break;
//				}
//				
////				username = userService.decrypt(channelId, encrytedUsername);
////				password = userService.decrypt(channelId, encrytedPassword);
//				String username = userService.aesDecrypt(channelId, encrytedUsername);
//				String password = userService.aesDecrypt(channelId, encrytedPassword);
//				
//				if (userService.checkPassword(username, password)) {
//					user = userService.getUser(username);
//				}
//				break;
//			}
			case USER_GUEST:
				{
					String encrytedMachineId = request.getMachineId();
					String machineId = null;
					if (StringUtils.isEmpty(encrytedMachineId)) {
						machineId = UUID.randomUUID().toString();
					} else {
						machineId = userService.decryptAES(channelId, encrytedMachineId);
					}
					if (userService.isValidMachineId(machineId)){
						usermap = userService.loginByMachineId(machineId);
						resp.setMachineId(machineId);
					}
					break;
				}
			case USER_THIRDPARTY:
				{
					String sdk = request.getSdk();
					String sdkUid = request.getSdkUid();
					String sdkToken = request.getSdkToken();
					String sdkChannel = request.getSdkChannel();
					
					usermap = userService.doLoginBySDK(sdkChannel, sdkUid);
					
					break;
				}
			default:
				{
					throw new Notify("User login type not supported!" + type.name());
				}
			}
			
			if (usermap != null) {
				int userid = Integer.parseInt(usermap.get(additional_field_userid));
				String username = usermap.get(field_user_name);
				
				UserPassport passport = userService.generatePassport(userid, username, usermap.get(field_user_create_time));
				
				resp.setUid(userid);
				resp.setPassport(passport.getIdentify());
				resp.setSecureKey(passport.getSecureKey());
				
				MessageUserSecureInfo secureInfo = new MessageUserSecureInfo();
				secureInfo.setUid(userid);
				secureInfo.setPassport(passport.getIdentify());
				secureInfo.setSecureKey(passport.getSecureKey());
				secureInfo.setChannelId(channelId);
				
				LoggerHelper.debug(logger, ()->String.format("User %d passport %s", userid, passport.getSecureKey()));
				
				//Game Server RPC call
				boolean result = userService.getRpcClientServiceFactory().getGamePlayerService().setPlayerSecureKey(secureInfo);
				
				logger.info("User {}/{}/{} login through channel {}, notify game server %s", userid, username, usermap.get(field_user_machine_id), channelId, result);
			} else {
				resp.setUid(-1);
			}
			
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			
			logger.error("User {}/{}/{} login decrypted failed through channel {}", request.getUsername(), request.getPassword(), request.getMachineId(), channelId, e);
		}
		catch (TimeoutException e) {
			logger.error("User {}/{}/{} login decrypted failed through channel {}", request.getUsername(), request.getPassword(), request.getMachineId(), channelId, e);
		} catch (Exception e) {
			logger.error("User {}/{}/{} login failed through channel {}", request.getUsername(), request.getPassword(), request.getMachineId(), channelId, e);
		}
		
		resp.setCode(usermap != null ? LoginCode.OK : LoginCode.ERROR);
		resp.setMsg(usermap != null ? "登陆成功！" : "用户名或密码错误！");
		
		if (usermap != null) {
			//remove client public key cache
			String key = userService.delUserClientKey(channelId);
			LoggerHelper.debug(logger, ()->String.format("Delete user %d client public key %s after login success. ", resp.getUid(), key));
			
			
			//TODO 登录成功后维护登录用户信息，方便数据统计
		}
		
		return resp;
	}
	
// MySQL version	
//	@Override
//	public ResponseUserLogin doLogin(RequestUserLogin request, String channelId) throws TException {
//		User user = null;
//		
//		ResponseUserLogin resp = new ResponseUserLogin();
//		try {
//			LoginType type = request.getType();
//			switch (type) {
//			case USER_ACCOUNT:
//			{
//				String encrytedUsername = request.getUsername();
//				String encrytedPassword = request.getPassword();
//				
//				if (encrytedUsername == null || encrytedPassword == null) {
//					break;
//				}
//				
////				username = userService.decrypt(channelId, encrytedUsername);
////				password = userService.decrypt(channelId, encrytedPassword);
//				String username = userService.aesDecrypt(channelId, encrytedUsername);
//				String password = userService.aesDecrypt(channelId, encrytedPassword);
//				
//				if (userService.checkPassword(username, password)) {
//					user = userService.getUser(username);
//				}
//				break;
//			}
//			case USER_GUEST:
//			{
//				String encrytedMachineId = request.getMachineId();
//				if (encrytedMachineId == null) {
//					break;
//				}
//				String machineId = userService.aesDecrypt(channelId, encrytedMachineId);
//				if (userService.isValidMachineId(machineId)){
//					user = userService.loginByMachineId(machineId);
//				}
//				break;
//			}
//			case USER_THIRDPARTY:
//			{
//				break;
//			}
//			default:
//				break;
//			}
//			
//			if (user != null) {
//				UserPassport passport = userService.generatePassport(user);
//				
//				resp.setUid(user.getId());
//				resp.setPassport(passport.getIdentify());
//				resp.setSecureKey(passport.getSecureKey());
//				
//				MessagePlayerSecureInfo secureInfo = new MessagePlayerSecureInfo();
//				secureInfo.setUid(user.getId());
//				secureInfo.setPassport(passport.getIdentify());
//				secureInfo.setSecureKey(passport.getSecureKey());
//				secureInfo.setChannelId(channelId);
//				
//				//game server RPC call
//				rpcClientServiceFactory.getGamePlayerService().setPlayerSecureKey(secureInfo);
//				
//				logger.info("User {}/{}/{}/{} login through channel {}", user.getId(), user.getUsername(), user.getPassword(), user.getMachineid(), channelId);
//			} else {
//				resp.setUid(-1);
//			}
//			
//			
//		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
//				| BadPaddingException | InvalidAlgorithmParameterException e) {
//			
//			logger.error("User {}/{}/{} login decrypted failed through channel {}", request.getUsername(), request.getPassword(), request.getMachineId(), channelId, e);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		resp.setCode(user != null ? LoginCode.OK : LoginCode.ERROR);
//		resp.setMsg(user != null ? "登陆成功！" : "用户名或密码错误！");
//		return resp;
//	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
