/**
 * 
 */
package com.live106.mars.game.rpc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.game.service.PlayerService;
import com.live106.mars.protocol.thrift.ResponseGameConnect;
import com.live106.mars.protocol.thrift.ReuqestGameConnect;
import com.live106.mars.protocol.thrift.game.IGamePlayerService.Iface;
import com.live106.mars.util.LoggerHelper;
import com.live106.mars.protocol.thrift.game.MessageUserSecureInfo;

/**
 * @author live106 @creation Oct 23, 2015
 *
 */
@Service
public class GamePlayerServiceRpc implements Iface {
	
	private static final Logger logger = LoggerFactory.getLogger(GamePlayerServiceRpc.class);
	
	@Autowired
	private PlayerService playerService;

	@Override
	public String ping(String visitor) throws TException {
		return "hello " + visitor;
	}

	@Override
	public boolean setPlayerSecureKey(MessageUserSecureInfo secureInfo) throws TException {
		boolean result = playerService.addPlayerSecureInfo(secureInfo);
		return result;
	}

	@Override
	public Map<com.live106.mars.protocol.thrift.ResponseGameConnect,Boolean> clientLogin(ReuqestGameConnect request) throws TException {
		ResponseGameConnect resp = new ResponseGameConnect();
		try {
			boolean result = playerService.checkUserConnect(request);
			resp.setResult(result);
			if (result) {
				//检查是否已有角色
				//生成随机角色名称,及角色id
				resp.setMsg("连接游戏服务器成功！");
			} else {
				resp.setMsg("通行证无效，请重新登录！");
			}
			
			LoggerHelper.debug(logger, () -> String.format("user %d connect to game server %s", request.getUid(), result));
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		Map<com.live106.mars.protocol.thrift.ResponseGameConnect,Boolean> retmap = new HashMap<>();
		retmap.put(resp, true);
		
		return retmap;
	}
	
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}
}
