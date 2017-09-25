package com.sunrun.threadPool;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class QueueHandler{
	
	private ThreadPoolFactory factory;

	public static ThreadPoolFactory getInstance(){
		ThreadPoolConfig config = ThreadPoolConfig.getInstance();
		config.setHandler(new MyRejectedExecutionHandler(msgQueue));
		// 线程池工厂
		return ThreadPoolFactory.getInstance(config);
	}
	public QueueHandler() {
		factory=getInstance();
		//scheduler.scheduleAtFixedRate(accessBufferThread, 0, 1, TimeUnit.SECONDS);
	}

	// 消息缓冲队列
	static Queue<Map<String, Object>> msgQueue = new LinkedList<Map<String, Object>>();
	private int size=0;
	private Runnable task;
	// 访问消息缓存的调度线程
	final Runnable accessBufferThread = new Runnable() {
		public void run() {
			// 查看是否有待定请求，如果有，则创建一个新的SomeServer，并添加到线程池中
			if (hasMoreAcquire()) {
				//System.out.println(msgQueue.size()+":msgQueue1");
				size=msgQueue.size()<=100?msgQueue.size():100;
				for (int j=0;j<size;j++) {
					//task = new SomeServer((Map<String, Object>)msgQueue.poll());
					//factory.addTask(task);
				}
			}
		}
	};

	// 调度线程池
	static final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(3);
	

	private boolean hasMoreAcquire() {
		return !msgQueue.isEmpty();
	}

}
