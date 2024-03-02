package com.sjj.cachespringbootstarter;

import com.alibaba.fastjson2.JSON;
import com.sjj.cachespringbootstarter.config.RedisDistributedProperties;
import com.sjj.cachespringbootstarter.core.CacheGetFilter;
import com.sjj.cachespringbootstarter.core.CacheGetIfAbsent;
import com.sjj.cachespringbootstarter.core.CacheLoader;
import com.sjj.cachespringbootstarter.toolkit.FastJson2Util;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * RedisTemplate 类的代理，增加分布式缓存能力<p>
 * 底层通过 {@link RedissonClient}、{@link StringRedisTemplate} 完成外观接口行为
 *
 * @author Island_World
 */
@RequiredArgsConstructor
public class StringRedisTemplateProxy implements DistributedCache {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final RedisDistributedProperties redisProperties;

    private static final String LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH = "lua/putIfAllAbsent.lua";
    private static final String SAFE_GET_DISTRIBUTED_LOCK_KEY_PREFIX = "safe_get_distributed_lock_get:";

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);
        // isAssignableFrom 检测 clazz 是不是 String 或其子类
        if (String.class.isAssignableFrom(clazz)) {
            return (T) value;
        }
        return JSON.parseObject(value, FastJson2Util.buildType(clazz));
    }


    @Override
    public void put(String key, Object value) {

    }


    @Override
    public Boolean putIfAllAbsent(Collection<String> keys) {
        return null;
    }


    @Override
    public Boolean delete(String key) {
        return null;
    }


    @Override
    public Long delete(Collection<String> keys) {
        return null;
    }


    @Override
    public Boolean hasKey(String key) {
        return null;
    }


    @Override
    public Object getInstance() {
        return null;
    }

    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {
        return null;
    }


    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter, CacheGetIfAbsent<String> cacheGetIfAbsent) {
        return null;
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter, CacheGetIfAbsent<String> cacheGetIfAbsent) {
        return null;
    }


    @Override
    public void put(String key, Object value, long timeout) {

    }


    @Override
    public void put(String key, Object value, long timeout, TimeUnit timeUnit) {

    }


    @Override
    public void safePut(String key, Object value, long timeout, RBloomFilter<String> bloomFilter) {

    }


    @Override
    public void safePut(String key, Object value, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {

    }


    @Override
    public Long countExistKeys(@NotNull String... keys) {
        return null;
    }



}
