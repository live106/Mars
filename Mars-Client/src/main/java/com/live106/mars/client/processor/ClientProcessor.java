package com.live106.mars.client.processor;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.live106.mars.client.ClientGroupRunner;
import com.live106.mars.client.ClientRunner;
import com.live106.mars.client.service.ClientService;
import com.live106.mars.protocol.handler.ProtocolProcessor;
import com.live106.mars.protocol.handler.annotation.Processor;
import com.live106.mars.protocol.handler.annotation.ProcessorMethod;
import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.thrift.LoginCode;
import com.live106.mars.protocol.thrift.PeerType;
import com.live106.mars.protocol.thrift.RequestSendClientPublicKey;
import com.live106.mars.protocol.thrift.RequestUserLogin;
import com.live106.mars.protocol.thrift.ResponseAuthServerPublicKey;
import com.live106.mars.protocol.thrift.ResponseSendClientPublicKey;
import com.live106.mars.protocol.thrift.ResponseUserLogin;
import com.live106.mars.protocol.thrift.ReuqestGameConnect;
import com.live106.mars.protocol.util.Cryptor;
import com.live106.mars.protocol.util.ProtocolCrypto;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Service
@Processor
@Scope(value="prototype")
public class ClientProcessor implements ProtocolProcessor {
	
	@Autowired
	private ClientService clientService;
	private byte[] serverPubKey;
	private byte[] secretKey;
	private Map<String, Object> keyMap;
	
	private Cryptor cryptor = new Cryptor(Cryptor.AES);
	
	@ProcessorMethod(messageClass = ResponseAuthServerPublicKey.class)
	public ProtocolBase receiveServerPublicKey(ChannelHandlerContext context, ResponseAuthServerPublicKey response) throws TException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalStateException {
		if (keyMap == null || keyMap.size() == 0) {
			keyMap = ProtocolCrypto.generateKey();
		}
		String serverKeyStr = response.getServerPubKey();
		this.serverPubKey = Base64.getDecoder().decode(serverKeyStr);
		secretKey = ProtocolCrypto.getSecretKey(this.serverPubKey, ProtocolCrypto.getPrivateKey(keyMap));
		
		RequestSendClientPublicKey request= new RequestSendClientPublicKey();
		request.setClientPubKey(Base64.getEncoder().encodeToString(ProtocolCrypto.getPublicKey(keyMap)));
		
		return ProtocolSerializer.serialize(request, new ProtocolPeer2Peer());
	}
	
	@ProcessorMethod(messageClass = ResponseSendClientPublicKey.class)
	public ProtocolBase receiveSendClientPublicKey(ChannelHandlerContext context, ResponseSendClientPublicKey response) throws Exception {
		boolean result = response.isResult();
		if (result) {
			String username = "admin";
			String password = "admin";
			
			cryptor.setSecretKey(ClientGroupRunner.secretKeys.get(context.channel().id().asLongText()));
			
			RequestUserLogin request= new RequestUserLogin();
//			byte[] encryptData = ProtocolCrypto.encrypt(username.getBytes(), this.secretKey);
			byte[] encryptData = cryptor.doCrypt(username.getBytes(), Cipher.ENCRYPT_MODE);
			request.setUsername(new String(Base64.getEncoder().encode(encryptData), StandardCharsets.UTF_8));
//			encryptData = ProtocolCrypto.encrypt(password.getBytes(), this.secretKey);
			encryptData = cryptor.doCrypt(password.getBytes(), Cipher.ENCRYPT_MODE);
			request.setPassword(Base64.getEncoder().encodeToString(encryptData));
			
			return ProtocolSerializer.serialize(request, new ProtocolPeer2Peer());
		} else {
			//FIXME log or throw exception
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
//		String uid = response.getUid();
//		String secureKey = response.getSecureKey();
//		String gameserver = response.getGameserver();
		
		clientService.setLoginResponse(response);
		
		System.err.println(code.name() + ":" + msg);
		
		ReuqestGameConnect request = new ReuqestGameConnect();
		
		ProtocolPeer2Peer protocol = new ProtocolPeer2Peer();
		protocol.getHeader().setTargetType(PeerType.PEER_TYPE_GAME);
//		protocol.setTargetType(PeerType.PEER_TYPE_GAME);
//		protocol.setTargetId(gameserver);
		return ProtocolSerializer.serialize(request, protocol);
	}

	public void setServerService(ClientService serverService) {
		this.clientService = serverService;
	}

}
