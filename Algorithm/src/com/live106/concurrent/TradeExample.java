/**
 * 
 */
package com.live106.concurrent;

import java.util.Queue;
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

	private final static Queue<String> strategies = new LinkedBlockingQueue<>();
	private final static byte[] strategyLock = new byte[0];

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// 初始化模拟数据
		dummyData();
		// 初始化线程池，线程池有很多参数可以配置，根据需要调整。
//		ExecutorService threadPool = Executors.newCachedThreadPool();
		ExecutorService threadPool =  new ThreadPoolExecutor(10, 50,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

		// 启动数据监听线程
		new Thread(() -> {
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
							System.err.println(String.format("处理订单[%s]完毕", order));
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}

			}
		}).start();

		synchronized (strategyLock) {
			strategyLock.notifyAll();
		}

		// 等待2s
		Thread.sleep(2000);

		// 增加新的模拟订单
		for (int i = 0; i < 100; i++) {
			strategies.add("newOrder" + i);
		}

		// 调用notify通知listener
		synchronized (strategyLock) {
			strategyLock.notifyAll();
		}

	}

	/**
	 * 生成模拟订单
	 */
	private static void dummyData() {
		for (int i = 0; i < 100; i++) {
			strategies.add(String.valueOf(i));
		}
	}

}
