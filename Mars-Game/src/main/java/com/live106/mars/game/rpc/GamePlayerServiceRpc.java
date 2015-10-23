/**
 * 
 */
package com.live106.mars.game.rpc;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.cache.game.GameCacheManager;
import com.live106.mars.protocol.thrift.game.IGamePlayerService.Iface;
import com.live106.mars.protocol.thrift.game.MessagePlayerSecureInfo;

/**
 * @author live106 @creation Oct 23, 2015
 *
 */
@Service
public class GamePlayerServiceRpc implements Iface {
	
	@Autowired
	private GameCacheManager gameCacheManager;

	@Override
	public String ping(String visitor) throws TException {
		return "hello " + visitor;
	}

	@Override
	public boolean setPlayerSecureKey(MessagePlayerSecureInfo secureInfo) throws TException {
		boolean result = gameCacheManager.addPlayerSecureInfo(secureInfo);
		return result;
	}

	public void setGameCacheManager(GameCacheManager gameCacheManager) {
		this.gameCacheManager = gameCacheManager;
	}

}
