/**
 * 
 */
package com.live106.mars.protocol.pojo;

import com.live106.mars.protocol.thrift.SerializeType;

/**
 * Netty协议消息统一接口
 * @author live106 @creation Oct 13, 2015
 */
public interface IProtocol {
	
	public static final int MAX_LENGTH = 2048;
	
	public final static byte PROTOCOL_P2P = 3;
	public final static byte PROTOCOL_TRANSFER = 5;

	public SerializeType getSerializeType();
	public int getProtocolHash();

}
