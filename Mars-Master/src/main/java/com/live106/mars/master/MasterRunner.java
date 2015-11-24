package com.live106.mars.master;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;

import com.live106.mars.master.config.MasterAppConfig;
import com.live106.mars.util.LoggerHelper;

/**
 * Master服务器的启动类
 * @author live106 @creation Oct 10, 2015
 */
@Controller
public class MasterRunner {
	
	private final static Logger logger = LoggerFactory.getLogger(MasterRunner.class);
	
	
	public static void main(String[] args) throws InterruptedException {
		
		SpringApplication.run(MasterAppConfig.class, args);
		
		LoggerHelper.info(logger, () -> String.format("Master server started at %1$s", new Date().toString()));
	}

}
