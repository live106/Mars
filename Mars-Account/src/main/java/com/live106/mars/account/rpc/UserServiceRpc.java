/**
 * 
 */
package com.live106.mars.account.rpc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.account.bean.UserPassport;
import com.live106.mars.account.db.model.User;
import com.live106.mars.account.service.RpcClientService;
import com.live106.mars.account.service.UserService;
import com.live106.mars.protocol.thrift.IUserService.Iface;
import com.live106.mars.protocol.thrift.LoginCode;
import com.live106.mars.protocol.thrift.RequestAuthServerPublicKey;
import com.live106.mars.protocol.thrift.RequestSendClientPublicKey;
import com.live106.mars.protocol.thrift.RequestUserLogin;
import com.live106.mars.protocol.thrift.ResponseAuthServerPublicKey;
import com.live106.mars.protocol.thrift.ResponseSendClientPublicKey;
import com.live106.mars.protocol.thrift.ResponseUserLogin;
import com.live106.mars.protocol.thrift.game.MessagePlayerSecureInfo;

/**
 * @author live106 @creation Oct 16, 2015
 *
 */
@Service
public class UserServiceRpc implements Iface {
	@Autowired
	private UserService userService;
	@Autowired
	private RpcClientService rpcClientService;

	@Override
	public String ping(String visitor) throws TException {
		return "hello " + visitor;
	}

	@Override
	public ResponseAuthServerPublicKey getPubKey(RequestAuthServerPublicKey request, String channelId) throws TException {
		ResponseAuthServerPublicKey resp = new ResponseAuthServerPublicKey();
		try {
			resp.setServerPubKey(userService.getServerPublicKeyBase64());
		} catch (NoSuchAlgorithmException e) {
			//
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public ResponseSendClientPublicKey sendPubKey(RequestSendClientPublicKey request, String channelId) throws TException {
		boolean result = false;
		String clientPubKey = request.getClientPubKey();
//		try {
//			userService.generateSecretKey(channelId, clientPubKey);
			userService.storeClientAesKey(channelId, clientPubKey);
			
			System.err.println("receive client pub key : " + channelId + "-->" + clientPubKey);
			result = true;
//		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | IllegalStateException e) {
//			e.printStackTrace();
//		}
		ResponseSendClientPublicKey resp = new ResponseSendClientPublicKey();
		resp.setResult(result);
		resp.setMsg("server has received client public key.");
		return resp;
	}

	@Override
	public ResponseUserLogin doLogin(RequestUserLogin request, String channelId) throws TException {
		String username = "";
		String password = "";
		boolean result = false;
		
		ResponseUserLogin resp = new ResponseUserLogin();
		try {
			String encrytedUsername = request.getUsername();
			String encrytedPassword = request.getPassword();
			
//			username = userService.decrypt(channelId, encrytedUsername);
//			password = userService.decrypt(channelId, encrytedPassword);
			username = userService.aesDecrypt(channelId, encrytedUsername);
			password = userService.aesDecrypt(channelId, encrytedPassword);
			
			result = userService.checkPassword(username, password);
			
			if (result || true) {
				User user = userService.getUser(username);
				UserPassport passport = userService.generatePassport(user);
				
				resp.setUid(user.getId());
				resp.setPassport(passport.getIdentify());
				resp.setSecureKey(passport.getSecureKey());
				
				MessagePlayerSecureInfo secureInfo = new MessagePlayerSecureInfo();
				secureInfo.setUid(user.getId());
				secureInfo.setPassport(passport.getIdentify());
				secureInfo.setSecureKey(passport.getSecureKey());
				secureInfo.setChannelId(channelId);
				
				//game server RPC call
				rpcClientService.getGamePlayerService().setPlayerSecureKey(secureInfo);
			}
			
			System.err.println("client login : " + result + " " + channelId + ": " +  username + "/" + password);
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			System.err.println("exception client : " + " " + channelId + ": " +  username + "/" + password);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.setCode(result ? LoginCode.OK : LoginCode.ERROR);
		resp.setMsg(result ? "登陆成功！" : "用户名或密码错误！");
		return resp;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setRpcClientService(RpcClientService rpcClientService) {
		this.rpcClientService = rpcClientService;
	}

}
