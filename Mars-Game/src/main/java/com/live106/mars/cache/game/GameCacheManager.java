/**
 * 
 */
package com.live106.mars.cache.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.live106.mars.protocol.thrift.game.MessagePlayerSecureInfo;

/**
 * @author live106 @creation Oct 23, 2015
 *
 */
@Service
public class GameCacheManager {
	private static Map<Integer, MessagePlayerSecureInfo> secureInfos = new ConcurrentHashMap<>();

	/**
	 * 
	 * @param secureInfo can not be null
	 * @return
	 */
	public boolean addPlayerSecureInfo(MessagePlayerSecureInfo secureInfo) {
		assert secureInfo != null : "secure info can not be null !";
		secureInfos.put(secureInfo.getUid(), secureInfo);
		return true;
	}

	public MessagePlayerSecureInfo getPlayerSecureInfo(Integer uid) {
		return secureInfos.get(uid);
	}
	
}
