package com.sjj.commonspringbootstarter.threadpool.build;

/*
 * 线程池 Builder 类
 *
 * @Author Island_World
 */

import com.sjj.commonspringbootstarter.toolkit.Assert;
import com.sjj.designpatternspringbootstarter.config.builder.Builder;

import java.math.BigDecimal;
import java.util.concurrent.*;

public class ThreadPoolBuilder implements Builder<ThreadPoolExecutor>{
    private int corePoolSize = calculateCoreNum();
    private int maximumPoolSize = corePoolSize + (corePoolSize>>1);
    private long keepAliveTime = 30000L;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    private BlockingQueue workQueue = new LinkedBlockingQueue(4096);
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
    private boolean isDaemon = false;
    private String threadNamePrefix;
    private ThreadFactory threadFactory;

    private int calculateCoreNum(){
        int cpuCoreNum = Runtime.getRuntime().availableProcessors();
        // 线程数 5:1 核心数
        return new BigDecimal(cpuCoreNum).divide(new BigDecimal("0.2")).intValue();
    }

    public ThreadPoolBuilder corePoolSize(int corePoolSize){
        this.corePoolSize = corePoolSize;
        return this;
    }

    public ThreadPoolBuilder maximumPoolSize(int maximumPoolSize){
        this.maximumPoolSize = maximumPoolSize;
        if(maximumPoolSize < this.corePoolSize){
            this.corePoolSize = maximumPoolSize;
        }
        return this;
    }

    public ThreadPoolBuilder keepAliveTime(long time){
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public ThreadPoolBuilder keepAliveTime(long time,TimeUnit timeUnit){
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        return this;
    }

    public ThreadPoolBuilder workQueue(BlockingQueue workQueue){
        this.workQueue = workQueue;
        return this;
    }

    public ThreadPoolBuilder rejected(RejectedExecutionHandler rejectedExecutionHandler){
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        return this;
    }

    public ThreadPoolBuilder threadFactory(String threadNamePrefix,Boolean isDaemon){
        this.threadNamePrefix = threadNamePrefix;
        this.isDaemon = isDaemon;
        return this;
    }
    public ThreadPoolBuilder threadFactory(ThreadFactory threadFactory){
        this.threadFactory = threadFactory;
        return this;
    }


    public ThreadPoolBuilder builder() {
        return new ThreadPoolBuilder();
    }

    @Override
    public ThreadPoolExecutor build() {
        if (null == threadFactory){
            Assert.notEmpty(threadNamePrefix,"threadNamePrefix must not be empty");
            threadFactory = new ThreadFactoryBuilder()
                    .prefix(threadNamePrefix)
                    .daemon(isDaemon)
                    .build();
        }
        ThreadPoolExecutor executorService;
        try {
            executorService = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    timeUnit,
                    workQueue,
                    threadFactory,
                    rejectedExecutionHandler);
        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Error creating thread pool parameter.", ex);
        }
        return executorService;
    }
}
