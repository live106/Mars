/**
 * 
 */
package com.live106.mars.protocol.pojo;

import org.apache.thrift.TException;

import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.buffer.ByteBuf;

/**
 * protocol between client and server
 * @author live106 @creation Oct 12, 2015
 *
 */
public class ProtocolPeer2Peer extends ProtocolBase {
	
	public ProtocolPeer2Peer() {
		super(PROTOCOL_P2P);
	}
	
	@Override
	public void encode(ByteBuf out) throws TException {
//		out.writeByte(super.getTargetType());
//		out.writeByte(super.getSourceType());
//		out.writeInt(super.getTargetId());
//		out.writeInt(super.getSourceId());
//		out.writeLong(super.getChannelId());
//		out.writeByte(super.getFlag());
//		out.writeByte(super.getSerializeType());
//		out.writeInt(super.getProtocolHash());
		
		byte[] headerBytes = ProtocolSerializer.serialize(this.header);
		out.writeInt(headerBytes.length);
		out.writeBytes(headerBytes);
		
		out.writeInt(this.data.length);
		out.writeBytes(this.data);
		
	}
	
	@Override
	public void decode(ByteBuf in) throws TException {
//		super.setTargetType(in.readByte());
//		super.setSourceType(in.readByte());
//		super.setTargetId(in.readInt());
//		super.setSourceId(in.readInt());
//		super.setChannelId(in.readLong());
//		super.setFlag(in.readByte());
//		super.setSerializeType(in.readByte());
//		super.setProtocolHash(in.readInt());
		
//		int len = in.readInt();
//		byte[] headerBytes = new byte[len];
//		in.readBytes(headerBytes);
//		
//		ProtocolSerializer.deserialize(this.header, headerBytes);
		
		int len = in.readInt();
		this.data = new byte[len];
		in.readBytes(this.data);
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
