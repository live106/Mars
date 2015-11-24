/**
 * 
 */
package com.live106.mars.master.processor;

import org.springframework.stereotype.Service;

import com.live106.mars.protocol.handler.ProtocolProcessor;
import com.live106.mars.protocol.handler.annotation.Processor;

/**
 * 账号协议处理类，已废弃，功能服务器协议处理全部改用RPC调用实现
 * @author live106 @creation Oct 9, 2015
 */
@Service
@Processor
@Deprecated
public class UserProcessor implements ProtocolProcessor {
//	@Autowired
//	private UserService userService;
//	 
//	@ProcessorMethod(messageClass = RequestAuthServerPublicKey.class)
//	public ProtocolBase handleRequestAuthServerPublicKey(ChannelHandlerContext context, RequestAuthServerPublicKey request) throws TException, NoSuchAlgorithmException {
//		
//		byte[] serverPublicKey = userService.getServerPublicKey();
//		
//		ResponseAuthServerPublicKey resp = new ResponseAuthServerPublicKey();
//		resp.setServerPubKey(Base64.getEncoder().encodeToString(serverPublicKey));
//		
//		return ProtocolSerializer.serialize(resp, new ProtocolPeer2Peer());
//	}
//	
//	@ProcessorMethod(messageClass = RequestSendClientPublicKey.class)
//	public ProtocolBase handleRequestSendClientPublicKey(ChannelHandlerContext context, RequestSendClientPublicKey request, ProtocolBase protocol) throws TException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, IllegalStateException {
//		String clientPubKey = request.getClientPubKey();
//		
//		userService.genSecretKey(protocol.getChannelId(), clientPubKey);
//		
//		ResponseSendClientPublicKey resp = new ResponseSendClientPublicKey();
//		resp.setResult(true);
//		resp.setMsg("");
//		
//		return ProtocolSerializer.serialize(resp, new ProtocolPeer2Peer());
//	}
//	
//	/**
//	 * 
//	struct RequestUserLogin
//	{
//		1: string username,
//		2: string password,
//		3: string sdkUid,
//		4: string sdkToken,
//		5: string machineId
//	}
//	
//	struct ResponseUserLogin
//	{
//		1: LoginCode code,
//		2: string msg,
//		3: string uid,
//		4: string secureKey,
//		5: string gameserver
//	}
//	 */
//	@ProcessorMethod(messageClass = RequestUserLogin.class)
//	public ProtocolBase handleRequestSendClientPublicKey(ChannelHandlerContext context, RequestUserLogin request, ProtocolBase protocol) throws TException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//		String encrytedUsername = request.getUsername();
//		String encrytedPassword = request.getPassword();
//		
//		long channelId = protocol.getChannelId();
//		String username = userService.decrypt(channelId, encrytedUsername);
//		String password = userService.decrypt(channelId, encrytedPassword);
//		
//		boolean result = userService.checkPassword(username, password);
//		
//		System.err.println("client login : " + result + " " + channelId + ": " +  username + "/" + password);
//		
//		ResponseUserLogin resp = new ResponseUserLogin();
//		resp.setCode(result ? LoginCode.OK : LoginCode.ERROR);
//		resp.setMsg(result ? "OK" : "用户名或密码错误！");
//		
//		//查找可用游戏服务器，生成加密key并通知
//		
//		return ProtocolSerializer.serialize(resp, new ProtocolPeer2Peer());
//	}
//
//	public void setUserService(UserService userService) {
//		this.userService = userService;
//	}
}
