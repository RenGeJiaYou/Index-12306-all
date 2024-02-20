package com.sjj.commonspringbootstarter.threadpool.support.eager;

import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 快速消费任务队列，继承自 LinkedBlockingQueue
 *
 * @Author Island_World
 */

public class TaskQueue<R extends Runnable> extends LinkedBlockingQueue<Runnable> {
    @Setter
    private EagerThreadPoolExecutor executor;

    public TaskQueue(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(Runnable runnable) {

        int currentPoolSize = executor.getPoolSize();
        // 线程池当前任务数 < 持有线程数 ,新建线程提交任务（父类默认行为，直接调 super）
        if (executor.getSubmittedTaskCount() < currentPoolSize) {
            super.offer(runnable);
        }
        // 线程池当前任务数量 < maximum ,新建线程提交任务（自定义）,根据线程池源码，会创建非核心线程
        if (currentPoolSize < executor.getMaximumPoolSize()) {
            return false;
        }

        // 线程池当前线程数量 ≥ maximum，执行拒绝策略（父类默认行为，直接调 super）
        return super.offer(runnable);
    }

    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(o, timeout, unit);
    }
}
