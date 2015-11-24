/**
 * 
 */
package com.live106.mars.protocol.pojo;

import org.apache.thrift.TException;

import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.buffer.ByteBuf;

/**
 * Protocol Message between Netty client and Netty server
 * @author live106 @creation Oct 12, 2015
 */
public class ProtocolPeer2Peer extends ProtocolBase {
	
	public ProtocolPeer2Peer() {
		super(PROTOCOL_P2P);
	}
	
	@Override
	public void encode(ByteBuf out) throws TException {
		byte[] headerBytes = ProtocolSerializer.serialize(this.header);
		out.writeInt(headerBytes.length);
		out.writeBytes(headerBytes);
		
		out.writeInt(this.data.length);
		out.writeBytes(this.data);
		
	}
	
	@Override
	public void decode(ByteBuf in) throws TException {
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
