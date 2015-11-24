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
 * 消息处理辅助包装类
 * @author live106 @creation Oct 16, 2015
 */
public class MessageProcessor<T> {
	private T processor;
	private Method method;
	private String messageBasePackage = "com.live106.mars.protocol.thrift";//TODO configurable
	private Class<?> messageClass;

	public MessageProcessor(T processor, Method method, String messageName) {
		this.processor = processor;
		this.method = method;
		try {
			messageClass = Class.forName(String.format("%s.%s", messageBasePackage, messageName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调用消息处理方法
	 * @param message
	 * @throws TException
	 */
	@SuppressWarnings("rawtypes")
	void invoke(ProtocolMessage message) throws TException {
		if (messageClass == null) {
			return;
		}
		
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
				TBase<?, ?> requestData = (TBase<?, ?>) messageClass.newInstance();
				TDeserializer td = new TDeserializer();
				td.deserialize(requestData, request.getData());

				//填充方法参数
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
						// TODO Check whether UID and passport are matching in the header if the target is GameServer
						args[1] = request.getHeader();
					}
					else if (parameterTypes[1].isPrimitive()) {
						args[1] = request.getSourceId();
					}
				}

				//调用消息处理方法
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
					} else {
						break;
					}
					//如果有返回协议则回写
					if (responseData != null) {
						ProtocolSerializer.serialize(responseData, response);
						context.writeAndFlush(response);
					}

				} while (false);

			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | TException e) {
				
				if (e instanceof InvocationTargetException) {
					InvocationTargetException ite = (InvocationTargetException) e;
					//发送异常协议
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

}
