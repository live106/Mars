/**
 * 
 */
package com.live106.mars.protocol.pojo;

import org.apache.thrift.TException;

import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

/**
 * @author live106 @creation Oct 13, 2015
 *
 */
public class ProtocolTranser extends ProtocolBase {

	private ByteBuf transferBody;
	
	public ProtocolTranser() {
		super(PROTOCOL_TRANSFER);
	}

	@Override
	public byte getProtocolType() {
		return PROTOCOL_TRANSFER;
	}

	@Override
	public void encode(ByteBuf out) throws TException {
		byte[] headerBytes = ProtocolSerializer.serialize(this.header);
		out.writeInt(headerBytes.length);
		out.writeBytes(headerBytes);
		out.writeBytes(this.transferBody);
		ReferenceCountUtil.release(this.transferBody);
	}

	public void decode(long channelId, ByteBuf in) {
		this.header.setChannelId(channelId);
		decode(in);
	}

	@Override
	public void decode(ByteBuf in) {
		int len = in.readableBytes();
		//FIXME is it a waste of memory or must be like this ?
		this.transferBody = PooledByteBufAllocator.DEFAULT.directBuffer(len);
		in.readBytes(this.transferBody);
//		in.getBytes(0, this.byteBuf, len);
//		in.skipBytes(in.readableBytes());
	}

	public ByteBuf getTransefreBody() {
		return transferBody;
	}
	
	@Override
	public SerializeType getSerializeType() {
		return header.getSerializeType();
	}
	
	@Override
	public int getProtocolHash() {
		return header.getProtocolHash();
	}

}
