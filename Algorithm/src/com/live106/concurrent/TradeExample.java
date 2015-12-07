/**
 * 
 */
package com.live106.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author live106 @creation Dec 2, 2015
 *
 */
public class TradeExample {

private static final int ORDER_COUNT = 100000;
	//	private final static Queue<String> strategies = new LinkedBlockingQueue<>();
	private final static Queue<String> strategies = new ConcurrentLinkedQueue<>();
	private final static byte[] strategyLock = new byte[0];

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// 初始化模拟数据
		dummyData();
		// 初始化线程池，线程池有很多参数可以配置，根据需要调整。
		ExecutorService lThreadPool = Executors.newCachedThreadPool();
//		ExecutorService threadPool = Executors.newCachedThreadPool();
		ExecutorService threadPool =  new ThreadPoolExecutor(2000, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
		
		CountDownLatch latch0 = new CountDownLatch(ORDER_COUNT);
		CountDownLatch latch1 = new CountDownLatch(ORDER_COUNT * 2);
		CountDownLatch latch2 = new CountDownLatch(ORDER_COUNT * 3);
		
		// 数据监听线程
		Runnable listener = () -> {
			while (true) {
				if (strategies.size() == 0) {
					synchronized (strategyLock) {
						try {
							strategyLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				String order = strategies.poll();
				if (order != null) {
					threadPool.submit(() -> {
						try {
							// 等待30ms模拟订单处理过程
							Thread.sleep(300);
//							System.err.println(String.format(Thread.currentThread() + "--->处理订单[%s]完毕", order));
							latch0.countDown();
							latch1.countDown();
							latch2.countDown();
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}

			}
		};
		
		lThreadPool.submit(listener);
		
		long startT = System.currentTimeMillis();

		synchronized (strategyLock) {
			strategyLock.notifyAll();
		}
		
		latch0.await();
		
		System.err.println("处理初始化消息消耗："  + (System.currentTimeMillis() - startT));

		{
			// 等待2s
			Thread.sleep(2000);
	
			startT = System.currentTimeMillis();
			// 增加新的模拟订单
			for (int i = 0; i < ORDER_COUNT; i++) {
				strategies.add("newOrder" + i);
				// 调用notify通知listener
				synchronized (strategyLock) {
					strategyLock.notifyAll();
				}
			}
			
			latch1.await();
			
			System.err.println("处理新消息消耗："  + (System.currentTimeMillis() - startT));
		}
		{
			// 等待2s
			Thread.sleep(2000);
			
			startT = System.currentTimeMillis();
			
			// 增加新的模拟订单
			for (int i = 0; i < ORDER_COUNT; i++) {
				strategies.add("newOrder" + i);
			}
			
			
			// 调用notify通知listener
			synchronized (strategyLock) {
				strategyLock.notifyAll();
			}
			
			latch2.await();
			
			System.err.println("处理新消息消耗："  + (System.currentTimeMillis() - startT));
		}

	}

	/**
	 * 生成模拟订单
	 */
	private static void dummyData() {
		for (int i = 0; i < ORDER_COUNT; i++) {
			strategies.add(String.valueOf(i));
		}
	}

}
