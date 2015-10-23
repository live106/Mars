package com.live106.mars.protocol.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.live106.mars.protocol.handler.annotation.ProcessorMethod;
import com.live106.mars.protocol.queue.ProtocolMQ;
import com.live106.mars.protocol.queue.ProtocolMessage;

/**
 * @author live106 @creation Oct 9, 2015
 *
 */
@Service
public class ProtocolMessageRPCDispacher {
	private volatile Map<Integer, MessageProcessor<?>> messageProcessors = new ConcurrentHashMap<>();
	private volatile Map<Integer, String> messageHashes = new ConcurrentHashMap<>();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static byte[] lock = new byte[0];
	
	public static void messageReceive() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	public void start(int threadNum) {
			executor.submit(messageListener);
//		for (int i = 0; i < threadNum; i++) {
//			executor.submit(messageListener);
//		}
	}
	
	Runnable messageListener = new Runnable() {
		public void run() {
			while (true) {	
				try {
					while (ProtocolMQ.getMessageCount() == 0) {
						synchronized (lock) {
							lock.wait();
						}
					}
					final ProtocolMessage message = ProtocolMQ.pollMessage();
					if (message != null) {
						executor.submit(() -> {
							
							System.err.println(Thread.currentThread().getName() + " handling message " + Integer.toHexString(message.getPojo().getProtocolHash()) + "-->" + messageHashes.get(message.getPojo().getProtocolHash()));
							
							MessageProcessor<?> messageProcessor = messageProcessors.get(message.getPojo().getProtocolHash());
							if (messageProcessor != null) {
								try {
									messageProcessor.invoke(message);
								} catch (Exception e) {
									//FIXME log
									e.printStackTrace();
								}
							}
						});
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	};

	/**
	 * 
	 * @param processors
	 *            processorName->processor's instance
	 */
	public void scanProcessor(Map<String, Object> processors) {
		for (String s : processors.keySet()) {
			Object obj = processors.get(s);

			Map<String, Method> methods = getMethodsWithAnnotation(obj.getClass(), ProcessorMethod.class);
			for (String messageName : methods.keySet()) {
				MessageProcessor<ProtocolProcessor> messageProcessor = new MessageProcessor<ProtocolProcessor>((ProtocolProcessor) obj, methods.get(messageName), messageName);
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

	public Map<Integer, MessageProcessor<?>> getMessageProcessors() {
		return messageProcessors;
	}
	public void setMessageProcessors(Map<Integer, MessageProcessor<?>> messageProcessors) {
		this.messageProcessors = messageProcessors;
	}

}
