package com.sjj.commonspringbootstarter.threadpool.support.eager;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 快速消费线程池
 *
 * @author Island_World
 */

public class EagerThreadPoolExecutor extends ThreadPoolExecutor {

    public EagerThreadPoolExecutor(int corePoolSize,
                                   int maximumPoolSize,
                                   long keepAliveTime, TimeUnit unit,
                                   TaskQueue<Runnable> workQueue,
                                   ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    // 添加任务时自增，任务异常及结束时递减
    private AtomicInteger submittedTaskCount = new AtomicInteger(0);

    public int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }

    @Override
    public void execute(Runnable command) {
        // 额外添加的功能：统计实际执行中的任务数量
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException ex) {
            // 捕获到满队异常，就再试一次
            TaskQueue taskQueue = (TaskQueue) super.getQueue();
            try {
                 /*抛出拒绝异常后就马上重试一下能不能直接加入队列
                 不能的话就直接抛异常，所以把超时时间设置为 0*/
                if (!taskQueue.retryOffer(command, 0, TimeUnit.SECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.", ex);
                }
            } catch (InterruptedException iex) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(iex);
            }
        } catch (Exception e) {
            submittedTaskCount.decrementAndGet();
            throw e;
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }
}
