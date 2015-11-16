/**
 * 
 */
package com.live106.mars.connector;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

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
		ch.pipeline().addLast(
				new ConnectorFrontendHandler(remoteHost, remotePort));
	}
}
