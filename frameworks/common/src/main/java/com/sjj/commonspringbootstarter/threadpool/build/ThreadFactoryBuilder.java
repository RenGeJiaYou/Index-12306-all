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

package com.sjj.commonspringbootstarter.threadpool.build;

/*
 * 线程工厂方法类
 *
 * @author Island_World
 */

import com.sjj.designpatternspringbootstarter.config.builder.Builder;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryBuilder implements Builder<ThreadFactory> {
    private static final long serialVersionUID = 1L;
    private ThreadFactory backingthreadFactory;
    private String namePrefix;
    private Boolean daemon;
    private Integer priority;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public ThreadFactoryBuilder threadFactory(ThreadFactory r){
        this.backingthreadFactory = r;
        return this;
    }

    public ThreadFactoryBuilder prefix(String namePrefix){
        this.namePrefix = namePrefix;
        return this;
    }

    public ThreadFactoryBuilder daemon(Boolean daemon){
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder priority(Integer priority){
        this.priority = priority;
        return this;
    }

    public ThreadFactoryBuilder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler){
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        return this;
    }

    @Override
    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder){
        final ThreadFactory backingThreadFactory = (null != builder.backingthreadFactory)
                ? builder.backingthreadFactory
                : Executors.defaultThreadFactory();
        final String namePrefix = builder.namePrefix;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler handler = builder.uncaughtExceptionHandler;
        // 体现参数间的依赖关系
        final AtomicLong count = (null == namePrefix)?null:new AtomicLong();

        // 返回一个 ThreadFactory 的实例类，而 ThreadFactory 是一个单方法接口，所以这里使用了 Lambda 表达式
        return r ->{
            final Thread thread = backingThreadFactory.newThread(r);
            // 体现对参数的校验
            if(null != namePrefix){
                thread.setName(namePrefix+"-"+count.getAndIncrement());
            }
            if (null!= daemon){
                thread.setDaemon(daemon);
            }
            if (null!=priority){
                thread.setPriority(priority);
            }
            if(null!=handler){
                thread.setUncaughtExceptionHandler(handler);
            }
            return thread;
        };
    }
}
