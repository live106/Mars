/**
 * 
 */
package com.live106.mars.protocol.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;

import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.queue.ProtocolMessage;
import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.protocol.util.ProtocolSerializer;

/**
 * @author live106 @creation Oct 16, 2015
 *
 */
public class MessageProcessor<T> {
	private T processor;
	private Method method;
	private String messageBasePackage = "com.live106.mars.protocol.thrift";//TODO
	private String messageName;
	
	public MessageProcessor(T processor, Method method, String messageName) {
		this.processor = processor;
		this.method = method;
		this.messageName = messageName;
	}

	void invoke(ProtocolMessage message) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException, TException {
		Object[] args = new Object[method.getParameterCount()];
		
		SerializeType serrializeType = message.getPojo().getSerializeType();
		switch (serrializeType) {
		case SERIALIZE_TYPE_THRIFT:
			{
				ProtocolPeer2Peer request = (ProtocolPeer2Peer) message.getPojo();
				Class<?> requestClass = Class.forName(String.format("%s.%s", messageBasePackage, messageName));
				TBase<?, ?> requestData = (TBase<?, ?>) requestClass.newInstance();
				TDeserializer td = new TDeserializer();
				td.deserialize(requestData, request.getData());
				
				args[0] = requestData;
				if (args.length > 1) {
					args[1] = message.getContext().channel().id().asLongText();
				}
				
				TBase<?, ?> responseData = (TBase<?, ?>) method.invoke(processor, args);
				
				ProtocolBase response = ProtocolSerializer.serialize(responseData, new ProtocolPeer2Peer());
				if (response != null) {
					message.getContext().writeAndFlush(response);
				}
				break;
			}
		case SERIALIZE_TYPE_PROTOBUFFER:
			{
				break;
			}
		default:
			break;
		}
		
	}

	public T getProcessor() {
		return processor;
	}

	public void setProcessor(T processor) {
		this.processor = processor;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getMessageBasePackage() {
		return messageBasePackage;
	}

	public void setMessageBasePackage(String messageBasePackage) {
		this.messageBasePackage = messageBasePackage;
	}

	public String getClassName() {
		return messageName;
	}

	public void setClassName(String className) {
		this.messageName = className;
	}
}
