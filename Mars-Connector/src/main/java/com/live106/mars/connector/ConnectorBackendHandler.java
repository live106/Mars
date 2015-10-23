/**
 * 
 */
package com.live106.mars.connector;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author None
 *
 */

@Sharable//TODO sharable or not?
public class ConnectorBackendHandler extends ChannelHandlerAdapter {

	private final Channel inboundChannel;

	public ConnectorBackendHandler(Channel inboundChannel) {
		this.inboundChannel = inboundChannel;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.read();
		ctx.write(Unpooled.EMPTY_BUFFER);
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) {
		inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				if (future.isSuccess()) {
					ctx.channel().read();
				} else {
					future.channel().close();
				}
			}
		});
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		// TODO
		// ConnectorFrontendHandler.closeOnFlush(inboundChannel);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		// ConnectorFrontendHandler.closeOnFlush(ctx.channel());
	}
}
