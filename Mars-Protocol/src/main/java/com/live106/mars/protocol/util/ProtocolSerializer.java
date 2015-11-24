/**
 * 
 */
package com.live106.mars.protocol.util;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TJSONProtocol;

import com.live106.mars.protocol.pojo.ProtocolBase;

/**
 * Thrift数据结构序列化操作包装类
 * @author live106 @creation Oct 13, 2015
 */
public class ProtocolSerializer {
	
	private static ThreadLocal<TSerializer> serializers = new ThreadLocal<TSerializer>(){
		@Override
		protected TSerializer initialValue() {
			TSerializer ts = new TSerializer();
			return ts;
		}
	};
	
	private static ThreadLocal<TDeserializer> deserializers = new ThreadLocal<TDeserializer>(){
		@Override
		protected TDeserializer initialValue() {
			TDeserializer ts = new TDeserializer();
			return ts;
		}
	};
	
	private static ThreadLocal<TSerializer> jsonserializers = new ThreadLocal<TSerializer>(){
		@Override
		protected TSerializer initialValue() {
			TSerializer ts = new TSerializer(new TJSONProtocol.Factory());
			return ts;
		}
	};
	
	private static ThreadLocal<TDeserializer> jsondeserializers = new ThreadLocal<TDeserializer>(){
		@Override
		protected TDeserializer initialValue() {
			TDeserializer ts = new TDeserializer(new TJSONProtocol.Factory());
			return ts;
		}
	};
	
	private static TSerializer getSerializer() {
		return serializers.get();
	}

	private static TDeserializer getDeserializer() {
		return deserializers.get();
	}
	
	private static TSerializer getJsonSerializer() {
		return jsonserializers.get();
	}
	
	private static TDeserializer getJsonDeserializer() {
		return jsondeserializers.get();
	}
	
	public static ProtocolBase serialize(TBase<?, ?> base, ProtocolBase protocol) throws TException {
		TSerializer ts = getSerializer();
		byte[] bytes = ts.serialize(base);
		protocol.getHeader().setProtocolHash(base.getClass().getSimpleName().hashCode());
		protocol.setData(bytes);

		return protocol;
	}

	public static byte[] serialize(TBase<?, ?> base) throws TException {
		TSerializer ts = getSerializer();
		return ts.serialize(base);
	}
	
	public static String serializeJson(TBase<?, ?> base) throws TException {
		TSerializer ts = getJsonSerializer();
		return ts.toString(base);
	}

	public static void deserialize(TBase<?, ?> base, byte[] data) throws TException {
		TDeserializer td = getDeserializer();
		td.deserialize(base, data);
	}
	
	public static void deserializeJson(TBase<?, ?> base, String data) throws TException {
		TDeserializer td = getJsonDeserializer();
		td.fromString(base, data);
	}

}
