/**
 * 
 */
package com.live106.mars.account;

import java.security.Security;
import java.util.Date;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;

import com.live106.mars.account.config.AccountAppConfig;

/**
 * <h1>账号服务器启动类</h1>
 * <br>
 * @author live106 @creation Oct 8, 2015
 */
@Controller
public class AccountRunner {
	
	private final static Logger logger = LoggerFactory.getLogger(AccountRunner.class);

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws InterruptedException, TException {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		
		SpringApplication.run(AccountAppConfig.class, args);
		
		logger.info("Account server started at {}.", new Date().toString());
	}

}
