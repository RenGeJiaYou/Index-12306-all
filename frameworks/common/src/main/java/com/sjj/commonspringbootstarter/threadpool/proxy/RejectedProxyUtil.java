package com.sjj.commonspringbootstarter.threadpool.proxy;

import cn.hutool.core.thread.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 拒绝策略代理工具类
 *
 * @author Island_World
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RejectedProxyUtil {
    public static RejectedExecutionHandler createProxy(RejectedExecutionHandler handler, AtomicLong rejectNum) {
        // 动态代理模式: 增强线程池拒绝策略，比如：拒绝任务报警或加入延迟队列重复放入等逻辑
        return (RejectedExecutionHandler) Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                new Class[]{RejectedProxyUtil.class},
                new RejectedProxyInvocationHandler(handler, rejectNum));
    }


    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        AtomicLong rejectedNum = new AtomicLong();
        RejectedExecutionHandler proxy = RejectedProxyUtil.createProxy(abortPolicy, rejectedNum);
        threadPoolExecutor.setRejectedExecutionHandler(proxy);
        for (int i = 0; i < 5; i++) {
            try {
                threadPoolExecutor.execute(() -> ThreadUtil.sleep(100000L));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        System.out.println("================ 线程池拒绝策略执行次数: " + rejectedNum.get());
    }
}
