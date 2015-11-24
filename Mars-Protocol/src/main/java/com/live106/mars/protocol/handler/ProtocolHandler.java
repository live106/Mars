package com.live106.mars.protocol.handler;

import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.queue.ProtocolMQ;
import com.live106.mars.protocol.queue.ProtocolMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Netty协议处理类，直接将协议消息加入待处理消息队列
 * @author live106 @creation Oct 9, 2015
 */

public class ProtocolHandler extends ChannelHandlerAdapter {
	
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
