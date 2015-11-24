package com.live106.mars.protocol.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.live106.mars.concurrent.MarsDefaultThreadFactory;
import com.live106.mars.protocol.queue.ProtocolMQ;
import com.live106.mars.protocol.queue.ProtocolMessage;
import com.live106.mars.util.LoggerHelper;

/**
 * 协议分发管理类
 * @author live106 @creation Oct 9, 2015
 */
@Service
public class ProtocolMessageRPCDispacher {

	private final static Logger logger = LoggerFactory.getLogger(ProtocolMessageRPCDispacher.class);

	/**
	 * 协议到处理类方法的映射
	 */
	private volatile Map<Integer, MessageProcessor<?>> messageProcessors = new ConcurrentHashMap<>();
	/**
	 * 协议hash到字符串名称的映射
	 */
	private volatile Map<Integer, String> messageHashes = new ConcurrentHashMap<>();
	/**
	 * 协议消息处理的线程池
	 */
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
	
	/**
	 * 协议坚挺线程
	 */
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
						
						//启动线程进行协议消息处理
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

	public Map<Integer, MessageProcessor<?>> getMessageProcessors() {
		return messageProcessors;
	}

	public Map<Integer, String> getMessageHashes() {
		return messageHashes;
	}

}
