package com.live106.mars.master;

import java.util.List;

import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.thrift.ProtocolHeader;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Netty消息解码
 * @author live106 @creation Nov 24, 2015
 *
 */
public class MasterProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.maxCapacity() == 0) {
			return;
		}
		
		int headerLen = in.readInt();
		if (headerLen == 0) {
			in.skipBytes(in.readableBytes());//XXX skip all ?
			return;
		}
		//XXX 可考虑优化整体流程设计，在此处避免对消息头的解析过程
		//解析原协议头
		byte[] header = new byte[headerLen];
		in.readBytes(header);
		ProtocolHeader pHeader = new ProtocolHeader();
		ProtocolSerializer.deserialize(pHeader, header);

		//组装转发协议
		ProtocolPeer2Peer pojo = new ProtocolPeer2Peer();
		pojo.setHeader(pHeader);
		pojo.decode(in);
		
		out.add(pojo);
	}

}
