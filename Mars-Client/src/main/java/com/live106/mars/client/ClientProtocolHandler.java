package com.live106.mars.client;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.queue.ProtocolMessage;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author live106 @creation Oct 9, 2015
 *
 */
@Service
@Scope(value="prototype")
@Sharable
public class ClientProtocolHandler extends ChannelHandlerAdapter {
	
	private ClientRunner clientRunner;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ProtocolBase pojo = (ProtocolBase) msg;
		ProtocolMessage message = new ProtocolMessage(pojo, ctx);
		clientRunner.addMessage(message);//TODO is it necessary to start a thread ?
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	public void setClientRunner(ClientRunner clientRunner) {
		this.clientRunner = clientRunner;
	}
	
}
