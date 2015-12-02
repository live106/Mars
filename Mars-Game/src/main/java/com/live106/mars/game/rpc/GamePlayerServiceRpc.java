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
import com.live106.mars.protocol.thrift.RequestBuyCommodity;
import com.live106.mars.protocol.thrift.RequestSimulateCharge;
import com.live106.mars.protocol.thrift.RequestSyncGold;
import com.live106.mars.protocol.thrift.ResponseBuyCommodity;
import com.live106.mars.protocol.thrift.ResponseGameConnect;
import com.live106.mars.protocol.thrift.ResponseSimulateCharge;
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

	/**
	 * 购买商品 《消消乐》
	 */
	@Override
	public Map<ResponseBuyCommodity, Boolean> buyCommondity(RequestBuyCommodity request, ProtocolHeader header)
			throws Notify, TException {
		//获取请求玩家用户ID
		int userId = header.getSourceId();
		//获取用户当前金币数量
		long gold = playerPayService.getGold(userId);
		//获取请求购买商品ID
		int commodityId = request.getCommodityId();
		//获取请求购买商品数量
		int number = request.getCommodityNumber();
		
		//记录日志
		LoggerHelper.debug(logger, () -> String.format("玩家%d拥有金币%d,请求购买商品%d数量%d.", userId, gold, commodityId, number));
		
		//调用购买商品接口，如果失败会抛出异常，系统会自动下发客户端提示协议Notify
		long goldLeft = playerPayService.buyCommondity(userId, commodityId, number);
		
		//组织返回协议内容
		ResponseBuyCommodity resp = new ResponseBuyCommodity();
		resp.setCommodityId(commodityId);
		resp.setCommodityNumber(number);
		resp.setResult(true);
		resp.setGoldLeft(goldLeft);
		
		//返回协议内容及是否要求客户端关闭SOCKET选项
		Map<ResponseBuyCommodity, Boolean> retmap = new HashMap<>();
		retmap.put(resp, true);
		
		return retmap;
	}
	
	/**
	 * 模拟充值，正式上线关闭
	 */
	@Override
	public Map<ResponseSimulateCharge, Boolean> simulateCharge(RequestSimulateCharge request, ProtocolHeader header)
			throws Notify, TException {
		int userId = header.getSourceId();
		long gold = request.getGold();
		long totalGold = playerPayService.doPay(userId, gold, "simulate charge");
		
		LoggerHelper.info(logger, ()->String.format("玩家%d模拟充值%d,充值后金币%d.", userId, gold, totalGold));
		
		ResponseSimulateCharge resp = new ResponseSimulateCharge();
		resp.setGold(totalGold);
		
		//返回协议内容及是否要求客户端关闭SOCKET选项
		Map<ResponseSimulateCharge, Boolean> retmap = new HashMap<>();
		retmap.put(resp, true);
		
		return retmap;
	}

}
