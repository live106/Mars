/**
 * 
 */
package com.live106.mars.game;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import com.live106.mars.game.config.GameAppConfig;

/**
 * @author live106 @creation Oct 23, 2015
 *
 */
public class GameRunner {

	private final static Logger logger = LoggerFactory.getLogger(GameRunner.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GameAppConfig.class, args);
		
		logger.info("Account server started at {}.", new Date().toString());
		
//		testDatabase();
	}

//	private static void testDatabase() {
//		PlayerService dao = SpringUtil.getBean(PlayerService.class);
//		Player player = dao.getPlayer(1);
//		System.err.println(player == null ? "nil" : player.getName());
//	}

}
