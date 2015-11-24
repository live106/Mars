/**
 * 
 */
package com.live106.mars.client;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.live106.mars.client.config.ClientAppConfig;
import com.live106.mars.client.console.Console;

/**
 * @author live106 @creation Oct 22, 2015
 *
 */
@Service
public class ClientGroupRunner {
	
	private final static Logger logger = LoggerFactory.getLogger(ClientGroupRunner.class);
	
	@Autowired
	public ConfigurableApplicationContext springContext;
	public static ConfigurableApplicationContext ctx;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ctx = SpringApplication.run(ClientAppConfig.class, args);
		
		Console.open();
		
		logger.info("ClientGroup server started at {}.", new Date().toString());
	}

}
