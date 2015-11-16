package com.live106.mars.protocol.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.live106.mars.concurrent.MarsDefaultThreadFactory;
import com.live106.mars.protocol.handler.annotation.ProcessorMethod;
import com.live106.mars.protocol.queue.ProtocolMQ;
import com.live106.mars.protocol.queue.ProtocolMessage;
import com.live106.mars.util.LoggerHelper;

/**
 * @author live106 @creation Oct 9, 2015
 *
 */
@Service
public class ProtocolMessageRPCDispacher {

	private final static Logger logger = LoggerFactory.getLogger(ProtocolMessageRPCDispacher.class);

	private volatile Map<Integer, MessageProcessor<?>> messageProcessors = new ConcurrentHashMap<>();
	private volatile Map<Integer, String> messageHashes = new ConcurrentHashMap<>();
	private static ExecutorService executor = Executors.newCachedThreadPool(new MarsDefaultThreadFactory("ml"));
	private static byte[] lock = new byte[0];

	public static void messageReceive() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void start() {
		executor.submit(messageListener);
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
						
						final String hashHex = Integer.toHexString(message.getPojo().getProtocolHash());
						final String messageString = messageHashes.get(message.getPojo().getProtocolHash());
						
						executor.submit(() -> {

							LoggerHelper.debug(logger,
									() -> String.format("%s handling message %s-->%s", Thread.currentThread().getName(), hashHex, messageString));

							MessageProcessor<?> messageProcessor = messageProcessors
									.get(message.getPojo().getProtocolHash());
							if (messageProcessor != null) {
								try {
									messageProcessor.invoke(message);
								} catch (TException e) {
									logger.error(String.format("%s error occured when handler message %s-->%s",
													Thread.currentThread().getName(),
													hashHex,
													messageString),
											e);
								}
							} else {
								LoggerHelper.debug(logger,
										() -> String.format("%s no handler found for message %s-->%s",
												Thread.currentThread().getName(),
												hashHex,
												messageString));
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
				MessageProcessor<ProtocolProcessor> messageProcessor = new MessageProcessor<ProtocolProcessor>(
						(ProtocolProcessor) obj, methods.get(messageName), messageName);
				messageProcessors.put(messageName.hashCode(), messageProcessor);
				messageHashes.put(messageName.hashCode(), messageName);
			}
		}
	}

	private Map<String, Method> getMethodsWithAnnotation(Class<? extends Object> processorClass,
			Class<? extends ProcessorMethod> annotationClass) {
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

	public Map<Integer, String> getMessageHashes() {
		return messageHashes;
	}

}
