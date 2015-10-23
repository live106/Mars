/**
 * 
 */
package com.live106.mars.protocol.util;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

import com.live106.mars.protocol.pojo.ProtocolBase;

/**
 * @author live106 @creation Oct 13, 2015
 *
 */
public class ProtocolSerializer {
	public static ProtocolBase serialize(TBase<?, ?> base, ProtocolBase protocol) throws TException {
		TSerializer ts = new TSerializer();
		byte[] bytes = ts.serialize(base);
		protocol.getHeader().setProtocolHash(base.getClass().getSimpleName().hashCode());
		protocol.setData(bytes);

		return protocol;
	}

	public static byte[] serialize(TBase<?, ?> base) throws TException {
		TSerializer ts = new TSerializer();
		return ts.serialize(base);
	}

	public static void deserialize(TBase<?, ?> base, byte[] data) throws TException {
		TDeserializer td = new TDeserializer();
		td.deserialize(base, data);
	}
}
