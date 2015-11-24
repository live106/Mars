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
 * <h1>游戏服务器启动类</h1>
 * <br>
 * @author live106 @creation Oct 23, 2015
 */
public class GameRunner {

	private final static Logger logger = LoggerFactory.getLogger(GameRunner.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GameAppConfig.class, args);
		
		logger.info("Game server started at {}.", new Date().toString());
	}

}
