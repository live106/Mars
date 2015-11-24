/**
 * 
 */
package com.live106.mars.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.protocol.thrift.game.IGamePlayerService;

/**
 * 管理RPC调用接口
 * @author live106 @creation Oct 23, 2015
 */
@Service
public class RpcClientServiceFactory {
	
	@Autowired
	private IGamePlayerService.Iface gamePlayerService;
	
	public IGamePlayerService.Iface getGamePlayerService() {
		return gamePlayerService;
	}

}
