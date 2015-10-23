package com.live106.mars.protocol.coder;

import java.util.List;

import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.thrift.ProtocolHeader;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() == 0) {
			//FIXME 
			return;
		}
		int headerLen = in.readInt();
		if (headerLen == 0) {
			in.skipBytes(in.readableBytes());//FIXME skip all ?
			return;
		}
		byte[] header = new byte[headerLen];
		in.readBytes(header);
		ProtocolHeader pHeader = new ProtocolHeader();
		ProtocolSerializer.deserialize(pHeader, header);
		
		ProtocolPeer2Peer pojo = new ProtocolPeer2Peer();
		pojo.setHeader(pHeader);
		pojo.decode(in);
		
		out.add(pojo);
	}

}
