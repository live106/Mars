package com.live106.mars.master;

import java.util.List;

import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.thrift.ProtocolHeader;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Netty��Ϣ����
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
		//XXX �ɿ����Ż�����������ƣ��ڴ˴��������Ϣͷ�Ľ�������
		//����ԭЭ��ͷ
		byte[] header = new byte[headerLen];
		in.readBytes(header);
		ProtocolHeader pHeader = new ProtocolHeader();
		ProtocolSerializer.deserialize(pHeader, header);

		//��װת��Э��
		ProtocolPeer2Peer pojo = new ProtocolPeer2Peer();
		pojo.setHeader(pHeader);
		pojo.decode(in);
		
		out.add(pojo);
	}

}
