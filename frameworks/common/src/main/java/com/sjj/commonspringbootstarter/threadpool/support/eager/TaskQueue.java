/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjj.commonspringbootstarter.threadpool.support.eager;

import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 快速消费任务队列，继承自 LinkedBlockingQueue
 *
 * @author Island_World
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
