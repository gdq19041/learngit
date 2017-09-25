package com.sunrun.threadPool;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工厂方法
 * @author 
 *
 */
public class ThreadPoolFactory {
     
    //线程池
    private static ThreadPoolExecutor  pool;    
    //自身对象
    private static ThreadPoolFactory factory;
     
    /**
     * 私有构造函数
     */
    private ThreadPoolFactory(){    }
     
    /**
     * 获取工厂对象
     * @param config
     * @return
     */
    public static ThreadPoolFactory getInstance(ThreadPoolConfig config){
        if(factory == null){
            factory = new ThreadPoolFactory();
        }
         
        if(pool == null){
             
            if(config.getHandler() == null){
                pool = new ThreadPoolExecutor(config.getCorePoolSize(),
                        config.getMaximumPoolSize(),config.getKeepAliveTime(),
                        config.getUnit(),config.getWorkQueue());
            }else{
                pool = new ThreadPoolExecutor(config.getCorePoolSize(),
                        config.getMaximumPoolSize(),config.getKeepAliveTime(),
                        config.getUnit(),config.getWorkQueue(),config.getHandler());
            }
        }       
        return factory;
    }
     
    /**
     * 添加线程池任务
     * @param run
     */
    public synchronized void addTask(Runnable run){
        pool.execute(run);
    }
     
    /**
     * 添加线程池任务
     * @param runs
     */
    public synchronized void addTask(List<Runnable> runs){
        if(runs != null){
            for(Runnable r:runs){
                this.addTask(r);
            }
        }
    }
     
    /**
     * 关闭线程池
     */
    public void closePool(){
        pool.shutdown();
    }
     
}
