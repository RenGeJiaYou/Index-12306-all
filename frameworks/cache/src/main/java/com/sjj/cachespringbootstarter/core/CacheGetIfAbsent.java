package com.sjj.cachespringbootstarter.core;

/**
 * 缓存查询为空管理器
 *
 * @author Island_World
 */

@FunctionalInterface
public interface CacheGetIfAbsent<T> {
    /**
     * 如果查询为空，则执行 execute()
     * */
    void execute(T param);
}
