/**
 * 
 */
package com.live106.mars.protocol.pojo;

import org.apache.thrift.TException;

import com.live106.mars.protocol.thrift.PeerType;
import com.live106.mars.protocol.thrift.ProtocolHeader;

import io.netty.buffer.ByteBuf;

/**
 * @author live106 @creation Oct 8, 2015
 *
 */
public abstract class ProtocolBase implements IProtocol {

	protected ProtocolHeader header = new ProtocolHeader();
	protected byte[] data = new byte[0];
	private byte protocolType;
	
	public ProtocolBase(byte protocolType) {
		this.protocolType = protocolType;
	}
	
	public abstract void encode(ByteBuf out) throws TException;
	public abstract void decode(ByteBuf in) throws TException;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public ProtocolHeader getHeader() {
		return header;
	}

	public void setHeader(ProtocolHeader header) {
		this.header = header;
	}

	public byte getProtocolType() {
		return protocolType;
	}
	
	public long getChannelId() {
		return header.getChannelId();
	}

	public PeerType getTargetType() {
		return header.getTargetType();
	}

	public int getTargetId() {
		return header.getTargetId();
	}

	public int getSourceId() {
		return header.getSourceId();
	}

	public PeerType getSourceType() {
		return header.getSourceType();
	}
}
