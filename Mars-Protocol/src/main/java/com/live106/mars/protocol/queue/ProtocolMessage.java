/**
 * 
 */
package com.live106.mars.protocol.queue;

import com.live106.mars.protocol.pojo.IProtocol;

import io.netty.channel.ChannelHandlerContext;

/**
 * 协议消息包装类
 * @author live106 @creation Oct 9, 2015
 */
public class ProtocolMessage {
	private IProtocol pojo;
	private ChannelHandlerContext context;
	
	public ProtocolMessage(IProtocol pojo, ChannelHandlerContext context) {
		this.pojo = pojo;
		this.context = context;
	}

	public ChannelHandlerContext getContext() {
		return context;
	}

	public void setContext(ChannelHandlerContext context) {
		this.context = context;
	}

	public IProtocol getPojo() {
		return pojo;
	}

	public void setPojo(IProtocol pojo) {
		this.pojo = pojo;
	}
}
