package com.live106.mars.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.live106.mars.protocol.handler.ProtocolProcessor;
import com.live106.mars.protocol.handler.annotation.ProcessorMethod;
import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.queue.ProtocolMessage;
import com.live106.mars.protocol.thrift.PeerType;
import com.live106.mars.protocol.thrift.ProtocolHeader;
import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.util.LoggerHelper;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * @author live106 @creation Oct 9, 2015
 *
 */
@Service
@Scope(value="prototype")
public class ProtocolMessageClientDispacher {
	
	private final static Logger logger = LoggerFactory.getLogger(ProtocolMessageClientDispacher.class);

	private Map<Integer, MessageProcessor> messageProcessors = new ConcurrentHashMap<>();
	private Map<Integer, String> messageHashes = new ConcurrentHashMap<>();
	
	//for client
	public void handleMessage(ProtocolMessage message) {
		if (!message.getContext().channel().isActive()) {
			message.getContext().disconnect();
			return;
		}
		MessageProcessor messageProcessor = messageProcessors.get(message.getPojo().getProtocolHash());
		if (messageProcessor == null) {
			//FIXME log
			return;
		}
		try {
			messageProcessor.invoke(message);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| ClassNotFoundException | InstantiationException | TException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	//
	
//	public void start() {
//		new Thread(new Runnable() {
//			public void run() {
//				while (true) {
//					try {
//						while (ProtocolMQ.getMessageCount() > 0) {
//							ProtocolMessage message = ProtocolMQ.pollMessage();
//							//FIXME 
//							if (!message.getContext().channel().isActive()) {
//								message.getContext().disconnect();
//								continue;
//							}
//							MessageProcessor messageProcessor = messageProcessors.get(message.getPojo().getProtocolHash());
//							if (messageProcessor == null) {
//								//FIXME log
//								continue;
//							}
//							try {
//								messageProcessor.invoke(message);
//							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
//									| ClassNotFoundException | InstantiationException | TException e) {
//								e.printStackTrace();
//							}
//						}
//						Thread.sleep(10);//FIXME 
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//	}

	/**
	 * 
	 * @param processors
	 *            processorName->processor's instance
	 */
	public void scanProcessor(Map<String, Object> processors) {
		for (String s : processors.keySet()) {
			Object obj = processors.get(s);
			if (!(obj instanceof ProtocolProcessor)) {
				LoggerHelper.debug(logger, () -> (String.format("%s is not a ProtocolProcessor!", obj.getClass())));
				continue;
			}
			ProtocolProcessor processor = (ProtocolProcessor) obj;
			if (processor.getListener() == null) {
				LoggerHelper.debug(logger, () -> (String.format("%s has no listener!", obj.getClass())));
				continue;
			}
			Map<String, Method> methods = getMethodsWithAnnotation(obj.getClass(), ProcessorMethod.class);
			for (String messageName : methods.keySet()) {
				MessageProcessor messageProcessor = new MessageProcessor((ProtocolProcessor) obj, methods.get(messageName));
				messageProcessors.put(messageName.hashCode(), messageProcessor);
				messageHashes.put(messageName.hashCode(), messageName);
			}
		}
	}

	private Map<String, Method> getMethodsWithAnnotation(Class<? extends Object> processorClass, Class<? extends ProcessorMethod> annotationClass) {
		Map<String, Method> messageMethods = new HashMap<>();
		Method[] methods = processorClass.getMethods();
		for (Method method : methods) {
			ProcessorMethod annotation = method.getAnnotation(annotationClass);
			if (annotation != null) {
				String messageName = annotation.messageClass().getSimpleName();
				messageMethods.put(messageName, method);
			}

		}
		return messageMethods;
	}

	class MessageProcessor {
		ProtocolProcessor processor;
		Method method;
		String messageBasePackage = "com.live106.mars.protocol.thrift";//TODO
		
		public MessageProcessor(ProtocolProcessor processor, Method method) {
			this.processor = processor;
			this.method = method;
		}

		void invoke(ProtocolMessage message) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException, TException, InterruptedException {
			ClientRunner runner = (ClientRunner) processor.getListener();
			if (runner == null) {
				return;
			}
			Object[] args = new Object[method.getParameterCount()];
			
			SerializeType serrializeType = message.getPojo().getSerializeType();
			switch (serrializeType) {
			case SERIALIZE_TYPE_THRIFT:
				{
					ProtocolPeer2Peer request = (ProtocolPeer2Peer) message.getPojo();
					String className = messageHashes.get(request.getProtocolHash());
					Class<?> requestClass = Class.forName(String.format("%s.%s", messageBasePackage, className));
					TBase<?, ?> requestData = (TBase<?, ?>) requestClass.newInstance();
					TDeserializer td = new TDeserializer();
					td.deserialize(requestData, request.getData());
					
//					ChannelHandlerContext context = message.getContext();
//					args[0] = context;
					args[0] = null;
					args[1] = requestData;
					if (args.length > 2) {
						args[2] = request;
					}
					
					ProtocolBase response = (ProtocolBase) method.invoke(processor, args);
					
					Channel channel = runner.connectChannelFulture().channel();
					
					if (request.getHeader().isCloseSocket()) {
						channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
						runner.doCloseSocket = true;
//							message.getContext().close();
					} 
					
					if (response != null) {
						ProtocolHeader header = response.getHeader();
						header.setChannelId(request.getChannelId());
//						header.setSourceType(request.getTargetType());
						if (header.getSourceId() < 0 && request.getTargetId() >= 0) {
							header.setSourceId(request.getTargetId());
						}
						if (header.getTargetId() <= 0) {
							header.setTargetId(request.getSourceId());
						}
						if (header.getTargetType() == PeerType.PEER_TYPE_DEFAULT) {
							header.setTargetType(request.getSourceType());
						}
						
						if (request.getHeader().isCloseSocket()) {
//							runner.reconnect();
							runner.doReconnect = true;
							runner.connectChannelFulture().channel().writeAndFlush(response);
//							runner.connectChannelFulture().addListener((ChannelFuture f) -> {
//								if (f.isSuccess()) {
//									f.channel().writeAndFlush(response);
//								} else {
//									//...
//								}
//							});
						} else {
							channel.writeAndFlush(response);
						}
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
			
//			if (args.length != method.getParameterCount()) {
//				throw new IllegalArgumentException("");
//			}
////			Class<?>[] parameterTypes = method.getParameterTypes();
//			method.invoke(processor, args);
		}
	}
}
