package com.live106.test;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.live106.mars.client.ClientRunner;
import com.live106.mars.client.config.ClientAppConfig;

public class LoginTest {

	private ConfigurableApplicationContext ctx;

	@Before
	public void initSpring() {
		ctx = SpringApplication.run(ClientAppConfig.class);
	}
	
	@Test
	public void login() {
		ClientRunner runner = ctx.getBean(ClientRunner.class);
		try {
			runner.start();
		} catch (NoSuchAlgorithmException | TException | InterruptedException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}
}
