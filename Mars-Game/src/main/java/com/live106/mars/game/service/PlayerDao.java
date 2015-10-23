/**
 * 
 */
package com.live106.mars.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.game.db.mapper.PlayerMapper;
import com.live106.mars.game.db.model.Player;

/**
 * @author live106 @creation Oct 8, 2015
 *
 */

@Service
public class PlayerDao {
	
	@Autowired
	private PlayerMapper playerMapper;
	
	public Player getPlayer(int id) {
		return playerMapper.selectByPrimaryKey(id);
	}

	public void setPlayerMapper(PlayerMapper playerMapper) {
		this.playerMapper = playerMapper;
	}
	
}
