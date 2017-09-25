package com.sunrun.threadPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池异常处理类
 * @author 
 *
 */
public class MyRejectedExecutionHandler implements RejectedExecutionHandler {
	private Queue<Map<String, Object>> msgQueue ;
	private ThreadPoolFactory factory;
	//private Runnable task;
	//private Map<String, Object> map;
    public MyRejectedExecutionHandler() {
		super();
	}

	public MyRejectedExecutionHandler(Queue<Map<String, Object>> msgQueue) {
		super();
		this.msgQueue = msgQueue;
	}

	@Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
		//map=new HashMap<String, Object>();
		/*map.put("parameter", ((SomeServer)task).getParameter());
    	map.put("session", ((SomeServer)task).getSession());*/
    	
		
		/*map.put("message", ((AccessDBThread)task).getMessage());
    	map.put("session", ((AccessDBThread)task).getSession());
    	msgQueue.offer(map);*/
    	//msgQueue.offer(((SomeServer)task).getParameter());
    	
    	//task = new SomeServer((Map<String, Object>)msgQueue.poll());
		factory= QueueHandler.getInstance();
		factory.addTask(task);
    }
}
