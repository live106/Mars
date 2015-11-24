/**
 * 
 */
package com.live106.mars.protocol.queue;

import java.util.concurrent.LinkedBlockingQueue;

import com.live106.mars.protocol.handler.ProtocolMessageRPCDispacher;

/**
 * <h2>Э����Ϣ���й�����</h2>
 * <p>���ڿɿ���ʹ��RabbitMQ��ZeroMQ��Disruptor�����Ч��</p>
 * <br>
 * @author live106 @creation Oct 9, 2015
 */
public class ProtocolMQ {
	
	private final static int QUEUE_CAPACITY = 1000;//TODO configurable
	
	private volatile static LinkedBlockingQueue<ProtocolMessage> messages = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
	
	public ProtocolMQ() {
	}
	
	public static ProtocolMessage pollMessage() {
		return messages.poll();//TODO consider whether timeout needed and compare method poll, take and peek
	}
	
	public static boolean addMessage(ProtocolMessage message) {
		boolean result = false;
		try {
			messages.put(message);//TODO consider queue capacity restrict and compare method add, offer and put
			ProtocolMessageRPCDispacher.messageReceive();//attention that this is a synchronized method
			result = true;
		} catch (InterruptedException e) {
			result = false;
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static int getMessageCount() {
		return messages.size();
	}
}
