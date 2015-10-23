package com.live106.mars.master;

import java.util.List;

import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.thrift.ProtocolHeader;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MasterProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.maxCapacity() == 0) {
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
		
//		switch (pHeader.getTargetType()) {
//		case PEER_TYPE_MASTER:
//			{
//				//decode package if target server is Master itself.
//				ProtocolPeer2Peer pojo = new ProtocolPeer2Peer();
//				pojo.setHeader(pHeader);
//				pojo.decode(in);
//				
//				out.add(pojo);
//				break;
//			}
//		case PEER_TYPE_ACCOUNT:
//		case PEER_TYPE_GAME:
//		case PEER_TYPE_CLIENT:
//			{
//				//transfer the package to the specify target.
//				ProtocolTranser pojo = new ProtocolTranser();
//				pojo.setHeader(pHeader);
//				if (pHeader.getSourceType() == PeerType.PEER_TYPE_CLIENT) {
//					pojo.decode(ctx.channel().id().asLongText().hashCode(), in);
//				} else {
//					pojo.decode(in);
//				}
//				out.add(pojo);
//				break;
//			}
//		default:
//			{
//				in.skipBytes(in.readableBytes());//FIXME skip all ?
//				break;
//			}
//		}
		
	}

}
