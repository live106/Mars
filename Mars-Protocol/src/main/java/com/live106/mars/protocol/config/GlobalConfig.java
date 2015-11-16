package com.live106.mars.protocol.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * 
 * @author live106 @creation Oct 16, 2015
 *
 */
public class GlobalConfig {
	
	public static String configFilename = "globalconfig.properties";
	private static String server_205 = "10.150.1.205";
	private static String server_206 = "10.150.1.206";
	private static String localhost = server_205;
//	private static String localhost = "localhost";
	
	public static String accountHost = localhost;
	public static int accountRpcPort = 10001;
	
	public static String gameHost = localhost;
	public static int gameRpcPort = 9000;
	
	public static String masterHost = localhost;
	public static int masterRpcPort = 9090;
	public static int masterNettyPort = 8080;
	
	public static String connectorHost = localhost;
	public static int connectorPort = 9999;
	
	public static String redisSentinelHost = server_206;
	public static int redisSentinelPort = 26379;
	
	//注意静态变量初始化顺序
	static {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(configFilename);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			
			accountHost = config.getString("account_host", localhost);
			accountRpcPort = config.getInt("account_rpc_port", 10001);
			gameHost = config.getString("game_host", localhost);
			gameRpcPort = config.getInt("game_rpc_port", 9000);
			masterHost = config.getString("master_host", localhost);
			masterRpcPort = config.getInt("master_rpc_port", 9090);
			masterNettyPort = config.getInt("master_netty_port", 8080);
			connectorHost = config.getString("connector_host", localhost);
			connectorPort = config.getInt("connector_port", 9999);
			redisSentinelHost = config.getString("redis_sentinel_host", server_206);
			redisSentinelPort = config.getInt("redus_sentinel_port", 26379);
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}
	}

}
