/**
 * 
 */
package com.live106.mars.connector;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 前端消息handler,直接将消息转发至后端Master进行处理
 * @author None
 */
public class ConnectorFrontendHandler extends ChannelHandlerAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(ConnectorFrontendHandler.class);
	
	private final String remoteHost;
	private final int remotePort;

	private volatile Channel outboundChannel;
	private Bootstrap bootstrap;
	private Channel inboundChannel;
	
	private static ScheduledThreadPoolExecutor scheduleExecutor = new ScheduledThreadPoolExecutor(5);

	public ConnectorFrontendHandler(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		inboundChannel = ctx.channel();

		if (bootstrap == null) {
			initBootstrap(ctx, inboundChannel);
		}
		connectMaster();
	}

	/**
	 * 初始化连接至Master的Netty客户端
	 * @param ctx
	 * @param inboundChannel
	 */
	private void initBootstrap(ChannelHandlerContext ctx, final Channel inboundChannel) {
		bootstrap = new Bootstrap();
		bootstrap.group(inboundChannel.eventLoop()).channel(ctx.channel().getClass())
				.handler(
						new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel ch) throws Exception {
								ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new ConnectorBackendHandler(inboundChannel));
							}
						})
				.option(ChannelOption.AUTO_READ, false);
	}
	/**
	 * 连接至Master
	 * @return
	 */
	private ChannelFuture connectMaster() {
		ChannelFuture f = bootstrap.connect(remoteHost, remotePort);
		outboundChannel = f.channel();
		f.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) {
				if (future.isSuccess()) {
					// connection complete start to read first data
					inboundChannel.read();
				} else {
					logger.debug(String.format("Channel %s start reconnect master in the schedule executor after 2 seconds", inboundChannel.id()));
					scheduleExecutor.schedule(() -> connectMaster(), 2, TimeUnit.SECONDS);
				}
			}
		});
		return f;
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) {
		if (outboundChannel.isActive()) {
			//数据写入后端服务器
			outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					if (future.isSuccess()) {
						// was able to flush out data, start to read the next
						// chunk
						ctx.channel().read();
					} else {
						future.channel().close();
					}
				}
			});
		} else {
			//TODO handle the message between disconnected and reconnected ?
			connectMaster();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (outboundChannel != null) {
			closeOnFlush(outboundChannel);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		closeOnFlush(ctx.channel());
	}

	/**
	 * Closes the specified channel after all queued write requests are flushed.
	 */
	static void closeOnFlush(Channel ch) {
		if (ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
