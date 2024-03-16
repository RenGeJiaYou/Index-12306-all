package com.sjj.cachespringbootstarter.core;
/**
 * 缓存加载器
 *
 * @author Island_World
 */

@FunctionalInterface
public interface CacheLoader<T> {
    /**
     * 加载缓存
     * */
    T load();
}
