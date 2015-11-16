package com.live106.mars.master;

import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.queue.ProtocolMQ;
import com.live106.mars.protocol.queue.ProtocolMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MasterProtocolHandler extends ChannelHandlerAdapter {
	
	//FIXME reconstruct this
//	public static final MasterChannelGroup channelGroup = new MasterChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
//		channelGroup.add(ctx.channel());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ProtocolBase pojo = (ProtocolBase) msg;
		
		//add to message queue if message is between master and server.
		ProtocolMessage message = new ProtocolMessage(pojo, ctx);
		ProtocolMQ.addMessage(message);//TODO is it necessary to start a thread ?
		
//		byte protocolType = pojo.getProtocolType();
//		switch (protocolType) {
//		case IProtocol.PROTOCOL_P2P:
//		{
//			//add to message queue if message is between master and server.
//			ProtocolMessage message = new ProtocolMessage(pojo, ctx);
//			ProtocolMQ.addMessage(message);//TODO is it necessary to start a thread ?
//			break;
//		}
//		case IProtocol.PROTOCOL_TRANSFER:
//		{
//			//transfer to the target server if needed.
//			MasterService service = SpringContextUtil.getBean(MasterService.class);
//			service.trasferProtocol((ProtocolTranser) pojo);
//			break;
//		}
//		default:
//			break;
//		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
