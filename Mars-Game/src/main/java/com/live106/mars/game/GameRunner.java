/**
 * 
 */
package com.live106.mars.game;

import org.springframework.boot.SpringApplication;

import com.live106.mars.game.config.GameAppConfig;
import com.live106.mars.game.db.model.Player;
import com.live106.mars.game.service.PlayerDao;
import com.live106.mars.game.util.SpringUtil;

/**
 * @author live106 @creation Oct 23, 2015
 *
 */
public class GameRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GameAppConfig.class, args);
		
//		testDatabase();
	}

	private static void testDatabase() {
		PlayerDao dao = SpringUtil.getBean(PlayerDao.class);
		Player player = dao.getPlayer(1);
		System.err.println(player == null ? "nil" : player.getName());
	}

}
