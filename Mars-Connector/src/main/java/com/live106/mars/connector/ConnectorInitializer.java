/**
 * 
 */
package com.live106.mars.connector;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author None
 *
 */
public class ConnectorInitializer extends ChannelInitializer<SocketChannel> {
	private final String remoteHost;
	private final int remotePort;

	public ConnectorInitializer(String remoteHost, int remotePort) {
	        this.remoteHost = remoteHost;
	        this.remotePort = remotePort;
	    }

	@Override
	public void initChannel(SocketChannel ch) {
		ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO),
				new ConnectorFrontendHandler(remoteHost, remotePort));
	}
}
