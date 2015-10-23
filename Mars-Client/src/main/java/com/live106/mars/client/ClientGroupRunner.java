/**
 * 
 */
package com.live106.mars.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	
	@Autowired
	public ConfigurableApplicationContext springContext;
	public static ConfigurableApplicationContext ctx;
	
	//FIXME just store the key data before Base64
	public static Map<String, String> secretKeys = new ConcurrentHashMap<>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ctx = SpringApplication.run(ClientAppConfig.class, args);
		
		Console.open();
		
//		testBeanScope(ctx);
	}

//	private static void testBeanScope(ConfigurableApplicationContext ctx) {
//		{
//			ClientRunner runner = ctx.getBean(ClientRunner.class);
//			System.err.println(runner);
//		}
//		{
//			ClientRunner runner = ctx.getBean(ClientRunner.class);
//			System.err.println(runner);
//		}
//		{
//			ClientGroupRunner runner = ctx.getBean(ClientGroupRunner.class);
//			System.err.println(runner);
//		}
//		{
//			ClientGroupRunner runner = ctx.getBean(ClientGroupRunner.class);
//			System.err.println(runner);
//		}
//	}

}
