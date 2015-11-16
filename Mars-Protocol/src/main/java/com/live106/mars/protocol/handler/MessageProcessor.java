/**
 * 
 */
package com.live106.mars.protocol.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;

import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.queue.ProtocolMessage;
import com.live106.mars.protocol.thrift.Notify;
import com.live106.mars.protocol.thrift.ProtocolHeader;
import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author live106 @creation Oct 16, 2015
 *
 */
public class MessageProcessor<T> {
	private T processor;
	private Method method;
	private String messageBasePackage = "com.live106.mars.protocol.thrift";// TODO
	private String messageName;

	public MessageProcessor(T processor, Method method, String messageName) {
		this.processor = processor;
		this.method = method;
		this.messageName = messageName;
	}

	@SuppressWarnings("rawtypes")
	void invoke(ProtocolMessage message) throws TException {
		ChannelHandlerContext context = message.getContext();
		if (context == null) {
			return;
		}
		
		SerializeType serrializeType = message.getPojo().getSerializeType();
		Object[] args = new Object[method.getParameterCount()];
		
		switch (serrializeType) {
		case SERIALIZE_TYPE_THRIFT: {
			try {
				ProtocolPeer2Peer request = (ProtocolPeer2Peer) message.getPojo();
				Class<?> requestClass = Class.forName(String.format("%s.%s", messageBasePackage, messageName));
				TBase<?, ?> requestData = (TBase<?, ?>) requestClass.newInstance();
				TDeserializer td = new TDeserializer();
				td.deserialize(requestData, request.getData());

				args[0] = requestData;
				if (args.length > 1) {
					// example -->
					// IUserService::CLogin.ResponseAuthServerPublicKey
					// getPubKey(1: CLogin.RequestAuthServerPublicKey request,
					// 2: string channelId)
					Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes[1].isAssignableFrom(String.class)) {
						args[1] = context.channel().id().asLongText();
					}
					// example --> IGameStoreService::storeLevel
					else if (parameterTypes[1].isAssignableFrom(ProtocolHeader.class)) {
						args[1] = request.getHeader();
					}
					// example -->
					else if (parameterTypes[1].isPrimitive()) {
						args[1] = request.getSourceId();
					}
				}

				Object obj = method.invoke(processor, args);

				do {
					if (obj == null) {
						break;
					}
					
					ProtocolPeer2Peer response = new ProtocolPeer2Peer();
					TBase<?, ?> responseData = null;
					
					if (obj instanceof Map) {
						Map map = (Map) obj;
						responseData = (TBase<?, ?>) map.keySet().iterator().next();
						response.getHeader().setCloseSocket((boolean) map.values().iterator().next());
					} else if (obj instanceof TBase) {
						responseData = (TBase<?, ?>) obj;
					}
					
					if (responseData != null) {
						ProtocolSerializer.serialize(responseData, response);
						context.writeAndFlush(response);
					}

				} while (false);

			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | TException e) {
				
				if (e instanceof InvocationTargetException) {
					InvocationTargetException ite = (InvocationTargetException) e;
					
					if (ite.getTargetException() instanceof Notify) {
						Notify notify = (Notify) ite.getTargetException();
						ProtocolPeer2Peer response = new ProtocolPeer2Peer();
						response.getHeader().setCloseSocket(true);
						
						ProtocolSerializer.serialize(notify, response);
						context.writeAndFlush(response);
					}
				}
			}

			break;
		}
		case SERIALIZE_TYPE_PROTOBUFFER: {
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
