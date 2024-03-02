package com.sjj.commonspringbootstarter.toolkit;

import lombok.SneakyThrows;

/**
 * 线程池工具类
 * <p>
 * 之所以封装一层，是为了用  @SneakyThrows 自动生成try-catch 捕获 InterruptedException 异常
 * 起到简化调用代码的作用
 *
 * @author Island_World
 */

public class ThreadUtil {
    /**
     * 睡眠当前线程指定时间 {@param millis}
     *
     * @param millis 睡眠时间，单位毫秒
     */
    @SneakyThrows(value = InterruptedException.class)
    public static void sleep(long millis) {
        Thread.sleep(millis);
    }
}
