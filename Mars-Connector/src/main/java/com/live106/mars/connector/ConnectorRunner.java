/**
 * 
 */
package com.live106.mars.connector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author None
 *
 */
public class ConnectorRunner {

	static final int LOCAL_PORT = 9999;
	static final String REMOTE_HOST = "localhost";
	static final int REMOTE_PORT = 8080;

	public static void main(String[] args) throws Exception {
		System.err.println("Proxying *:" + LOCAL_PORT + " to " + REMOTE_HOST + ':' + REMOTE_PORT + " ...");

		// Configure the bootstrap.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ConnectorInitializer(REMOTE_HOST, REMOTE_PORT))
					.childOption(ChannelOption.AUTO_READ, false).bind(LOCAL_PORT).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
		//connect to Master
//		ConnectMaster();
	}

//	private static void ConnectMaster() {
//		// Start the connection attempt.
//		Bootstrap b = new Bootstrap();
//		b.group(inboundChannel.eventLoop()).channel(ctx.channel().getClass())
//				.handler(new ConnectorBackendHandler(inboundChannel)).option(ChannelOption.AUTO_READ, false);
//		ChannelFuture f = b.connect(remoteHost, remotePort);
//		outboundChannel = f.channel();
//		f.addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture future) {
//				if (future.isSuccess()) {
//					// connection complete start to read first data
//					inboundChannel.read();
//				} else {
//					// Close the connection if the connection attempt has
//					// failed.
//					inboundChannel.close();
//				}
//			}
//		});
//	}

}
