package com.sjj.cachespringbootstarter;

import com.sjj.cachespringbootstarter.core.CacheGetFilter;
import com.sjj.cachespringbootstarter.core.CacheGetIfAbsent;
import jakarta.validation.constraints.NotNull;
import org.redisson.api.RBloomFilter;
import com.sjj.cachespringbootstarter.core.CacheLoader;
import jakarta.validation.constraints.NotBlank;

import java.util.concurrent.TimeUnit;

/**
 * 分布式缓存
 *
 * @author Island_World
 */

public interface DistributedCache extends Cache {

    /**
     * 获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     */
    <T> T get(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout);

    /**
     * 获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param timeUnit
     */
    <T> T get(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存击穿、缓存雪崩场景，适用于不被外部直接调用的接口
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存击穿、缓存雪崩场景，适用于不被外部直接调用的接口
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param timeUnit
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存穿透、缓存击穿以及缓存雪崩场景，需要客户端传递布隆过滤器，适用于被外部直接调用的接口
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param bloomFilter
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存穿透、缓存击穿以及缓存雪崩场景，需要客户端传递布隆过滤器，适用于被外部直接调用的接口
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param timeUnit
     * @param bloomFilter
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存穿透、缓存击穿以及缓存雪崩场景，需要客户端传递布隆过滤器，并通过 {@link CacheGetFilter} 解决布隆过滤器无法删除问题，适用于被外部直接调用的接口
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param bloomFilter
     * @param filter
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存穿透、缓存击穿以及缓存雪崩场景，需要客户端传递布隆过滤器，并通过 {@link CacheGetFilter} 解决布隆过滤器无法删除问题，适用于被外部直接调用的接口
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param timeUnit
     * @param bloomFilter
     * @param filter
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter,CacheGetFilter<String> filter);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存穿透、缓存击穿以及缓存雪崩场景，需要客户端传递布隆过滤器，并通过 {@link CacheGetFilter} 解决布隆过滤器无法删除问题，适用于被外部直接调用的接口
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param bloomFilter
     * @param filter
     * @param cacheGetIfAbsent
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter, CacheGetIfAbsent<String> cacheGetIfAbsent);

    /**
     * 以一种"安全"的方式获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     * 通过此方式防止程序中可能出现的：缓存穿透、缓存击穿以及缓存雪崩场景，需要客户端传递布隆过滤器，并通过 {@link CacheGetFilter} 解决布隆过滤器无法删除问题，适用于被外部直接调用的接口
     *
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param timeUnit
     * @param bloomFilter
     * @param filter
     * @param cacheGetIfAbsent
     */
    <T> T saftGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter,CacheGetFilter<String> filter,CacheGetIfAbsent<String> cacheGetIfAbsent);


    /**
     * 放入缓存，自定义超时时间
     *
     * @param key
     * @param value
     * @param timeout
     */
    void put(@NotBlank String key,Object value,long timeout);

    /**
     * 放入缓存，自定义超时时间
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    void put(@NotBlank String key,Object value,long timeout,TimeUnit timeUnit);

    /**
     * 放入缓存，自定义超时时间
     * 通过此方式防止程序中可能出现的：缓存穿透、缓存击穿以及缓存雪崩场景，需要客户端传递布隆过滤器，适用于被外部直接调用的接口
     *
     * @param key
     * @param value
     * @param timeout
     * @param bloomFilter
     */
    void safePut(@NotBlank String key,Object value,long timeout,RBloomFilter<String> bloomFilter);

    /**
     * 放入缓存，自定义超时时间，并将 key 加入布隆过滤器。极大概率通过此方式防止：缓存穿透、缓存击穿、缓存雪崩
     * 需要客户端传递布隆过滤器，适用于被外部直接调用的接口
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @param bloomFilter
     */
    void safePut(@NotBlank String key,Object value,long timeout,TimeUnit timeUnit,RBloomFilter<String> bloomFilter);

    /**
     * 统计指定 key 的存在数量
     *
     * @param keys
     * */
    Long countExistKeys(@NotNull String... keys);
}
