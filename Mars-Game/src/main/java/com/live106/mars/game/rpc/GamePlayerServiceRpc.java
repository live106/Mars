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

import com.live106.mars.game.service.PlayerPayService;
import com.live106.mars.game.service.PlayerService;
import com.live106.mars.protocol.thrift.Notify;
import com.live106.mars.protocol.thrift.ProtocolHeader;
import com.live106.mars.protocol.thrift.RequestSyncGold;
import com.live106.mars.protocol.thrift.ResponseGameConnect;
import com.live106.mars.protocol.thrift.ResponseSyncGold;
import com.live106.mars.protocol.thrift.ReuqestGameConnect;
import com.live106.mars.protocol.thrift.game.IGamePlayerService.Iface;
import com.live106.mars.protocol.thrift.game.MessageUserSecureInfo;
import com.live106.mars.util.LoggerHelper;

/**
 * 玩家RPC服务实现类
 * @author live106 @creation Oct 23, 2015
 */
@Service
public class GamePlayerServiceRpc implements Iface {
	
	private static final Logger logger = LoggerFactory.getLogger(GamePlayerServiceRpc.class);
	
	@Autowired
	private PlayerService playerService;
	@Autowired
	private PlayerPayService playerPayService;

	@Override
	public String ping(String visitor) throws TException {
		return "hello " + visitor;
	}

	/**
	 * 保存玩家安全信息
	 */
	@Override
	public boolean setPlayerSecureKey(MessageUserSecureInfo secureInfo) throws TException {
		boolean result = playerService.addPlayerSecureInfo(secureInfo);
		return result;
	}

	/**
	 * 处理玩家连接游戏服
	 */
	@Override
	public Map<com.live106.mars.protocol.thrift.ResponseGameConnect,Boolean> clientLogin(ReuqestGameConnect request) throws TException {
		ResponseGameConnect resp = new ResponseGameConnect();
		try {
			boolean result = playerService.checkUserConnect(request);
			resp.setResult(result);
			if (result) {
				//记录玩家登陆数据
				resp.setMsg("连接游戏服务器成功！");
			} else {
				resp.setMsg("通行证无效，请重新登录！");
			}
			
			LoggerHelper.debug(logger, () -> String.format("User %d connect to game server %s", request.getUid(), result));
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		Map<com.live106.mars.protocol.thrift.ResponseGameConnect,Boolean> retmap = new HashMap<>();
		retmap.put(resp, true);
		
		return retmap;
	}
	
	/**
	 * 处理玩家充值数据
	 * TODO 处理相同订单重复调用情况
	 */
	@Override
	public boolean pay(long userId, int amount, String orderId, String plainData) throws Notify, TException {
		if (amount <= 0) {
			return true;
		}
		playerPayService.doPay(userId, amount, orderId);
		//订单号加入待处理队列，等待客户端请求
		playerPayService.addOrder(orderId);
		
		return true;
	}
	
	/**
	 * 处理同步金币数据请求
	 */
	@Override
	public Map<ResponseSyncGold, Boolean> syncGold(RequestSyncGold request, ProtocolHeader header) throws Notify, TException {
		int userId = header.getSourceId();
		long gold = playerPayService.getGold(userId);
		
		LoggerHelper.debug(logger, () -> String.format("同步玩家%d金币数量%d.", userId, gold));
		
		ResponseSyncGold resp = new ResponseSyncGold();
		resp.setGold(gold);
		
		Map<ResponseSyncGold, Boolean> retmap = new HashMap<>();
		retmap.put(resp, true);
		return retmap;
	}
	
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

}
