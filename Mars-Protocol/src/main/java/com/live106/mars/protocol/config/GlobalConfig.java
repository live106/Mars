package com.live106.mars.protocol.config;

/**
 * 
 * @author live106 @creation Oct 16, 2015
 *
 */
public class GlobalConfig {
	private static final String server_205 = "10.150.1.205";
	private static final String localhost = "localhost";
	
	public static final String accountRpcHost = localhost;
	public static final int accountRpcPort = 8090;
	public static final String gameRpcHost = localhost;
	public static final int gameRpcPort = 9000;
	
	public static final String masterHost = localhost;
	public static final int masterRpcPort = 9090;
	public static final int masterNettyPort = 8080;
	
	public static final String connectorHost = localhost;
	public static final int connectorPort = 9999;
}
