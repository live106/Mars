package com.live106.mars.master;

import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.queue.ProtocolMQ;
import com.live106.mars.protocol.queue.ProtocolMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Netty消息处理，直接加入待处理消息队列
 * @author live106 @creation Nov 24, 2015
 *
 */
public class MasterProtocolHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ProtocolBase pojo = (ProtocolBase) msg;
		
		ProtocolMessage message = new ProtocolMessage(pojo, ctx);
		ProtocolMQ.addMessage(message);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
