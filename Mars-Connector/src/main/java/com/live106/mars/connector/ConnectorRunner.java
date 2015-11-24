/**
 * 
 */
package com.live106.mars.connector;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * <h1>���������������</h1>
 * @author None
 */
public class ConnectorRunner {

	private final static Logger logger = LoggerFactory.getLogger(ConnectorRunner.class);

	static final int LOCAL_PORT = 9999;
	static final String REMOTE_HOST = "localhost";
	static final int REMOTE_PORT = 8080;

	public static void main(String[] args) throws Exception {
		
		logger.info("Connector proxying *:{} to {}:{}", LOCAL_PORT, REMOTE_HOST, REMOTE_PORT);

		new Thread(() -> {
			// Configure the bootstrap.
			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
						.handler(new LoggingHandler(LogLevel.INFO))
						.childHandler(new ConnectorInitializer(REMOTE_HOST, REMOTE_PORT))
						.childOption(ChannelOption.AUTO_READ, false)
						
						.childOption(ChannelOption.TCP_NODELAY, true)
						.childOption(ChannelOption.SO_KEEPALIVE, true)
//						.childOption(ChannelOption.SO_BACKLOG, 128)
						/*
						.childOption(ChannelOption.SO_SNDBUF, 8192)
						.childOption(ChannelOption.SO_RCVBUF, 8192)
						.childOption(ChannelOption.SO_TIMEOUT, 5000)
						.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
						//�ڵ���close�����󣬽���౻����n�롣����n���ڣ�ϵͳ��������δ�ͳ������ݰ����ͳ�ȥ�����������n�룬�������δ���͵����ݰ�����Щ���ݰ���ȫ������������close�������������ء������linger��Ϊ0���͹ر�SO_LINGERѡ���������һ���ġ�
						.childOption(ChannelOption.SO_LINGER, 0)
						.childOption(ChannelOption.SO_REUSEADDR, true)
						.childOption(ChannelOption.SO_BROADCAST, true)
						*/
						
						.bind(LOCAL_PORT).sync().channel().closeFuture()
						.sync();
			} catch (Exception e) {
				logger.error("Connector netty server started failed at {}.", new Date().toString(), e);
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		}, "netty-server-connector").start();

		logger.info("Connector server started at {}.", new Date().toString());
		
		// connect to Master
		// ConnectMaster();
	}

	// private static void ConnectMaster() {
	// // Start the connection attempt.
	// Bootstrap b = new Bootstrap();
	// b.group(inboundChannel.eventLoop()).channel(ctx.channel().getClass())
	// .handler(new
	// ConnectorBackendHandler(inboundChannel)).option(ChannelOption.AUTO_READ,
	// false);
	// ChannelFuture f = b.connect(remoteHost, remotePort);
	// outboundChannel = f.channel();
	// f.addListener(new ChannelFutureListener() {
	// @Override
	// public void operationComplete(ChannelFuture future) {
	// if (future.isSuccess()) {
	// // connection complete start to read first data
	// inboundChannel.read();
	// } else {
	// // Close the connection if the connection attempt has
	// // failed.
	// inboundChannel.close();
	// }
	// }
	// });
	// }

}
