package com.sjj.cachespringbootstarter.core;

/**
 * 缓存查询过滤器
 *
 * @author Island_World
 */

@FunctionalInterface
public interface CacheGetFilter<T> {
    /**
     * 缓存过滤器
     *
     * @param param 输出参数
     * @return {@code true} 如果输入参数匹配，否则 {@link Boolean#FALSE}
     * */
    boolean filter(T param);
}
