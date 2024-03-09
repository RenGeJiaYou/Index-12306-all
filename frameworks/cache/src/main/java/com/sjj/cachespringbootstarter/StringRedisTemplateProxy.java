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

package com.sjj.cachespringbootstarter;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.sjj.basespringbootstarter.Singleton;
import com.sjj.cachespringbootstarter.config.RedisDistributedProperties;
import com.sjj.cachespringbootstarter.core.CacheGetFilter;
import com.sjj.cachespringbootstarter.core.CacheGetIfAbsent;
import com.sjj.cachespringbootstarter.core.CacheLoader;
import com.sjj.cachespringbootstarter.toolkit.CacheUtil;
import com.sjj.cachespringbootstarter.toolkit.FastJson2Util;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collection;
import java.util.Optional;
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
        // String 类就直接返回
        if (String.class.isAssignableFrom(clazz)) {
            return (T) value;
        }
        // 非 String 类 就返回对应的对象实例
        return JSON.parseObject(value, FastJson2Util.buildType(clazz));
    }


    @Override
    public void put(String key, Object value) {
        put(key, value, redisProperties.getValueTimeout());
    }


    @Override
    public Boolean putIfAllAbsent(Collection<String> keys) {
        DefaultRedisScript<Boolean> script = Singleton.get(LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH, () -> {
            DefaultRedisScript redisScript = new DefaultRedisScript();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH)));
            redisScript.setResultType(Boolean.class);
            return redisScript;
        });

        Boolean result = stringRedisTemplate.execute(script, Lists.newArrayList(keys), redisProperties.getValueTimeout().toString());
        return result != null && result;
    }


    @Override
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }


    @Override
    public Long delete(Collection<String> keys) {
        return stringRedisTemplate.delete(keys);
    }


    @Override
    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }


    @Override
    public Object getInstance() {
        return stringRedisTemplate;
    }

    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {

        return get(key, clazz, cacheLoader, timeout, redisProperties.getValueTimeUnit());
    }


    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        T result = get(key, clazz);
        if (!CacheUtil.isNullOrBlank(result)) {
            return result;
        }
        return loadAndSet(key, cacheLoader, timeout, timeUnit, false, null);
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {
        return saftGet(key, clazz, cacheLoader, timeout,redisProperties.getValueTimeUnit());
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        return saftGet(key, clazz, cacheLoader, timeout, timeUnit, null);
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter) {
        return saftGet(key, clazz, cacheLoader, timeout, redisProperties.getValueTimeUnit(), bloomFilter, null);
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        return saftGet(key, clazz, cacheLoader, timeout, timeUnit, bloomFilter, null);
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter) {
        return saftGet(key, clazz, cacheLoader, timeout, redisProperties.getValueTimeUnit(), bloomFilter, filter, null);
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter) {
        return saftGet(key, clazz, cacheLoader, timeout, timeUnit, bloomFilter, filter, null);
    }


    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> filter, CacheGetIfAbsent<String> cacheGetIfAbsent) {
        return saftGet(key, clazz, cacheLoader, timeout, redisProperties.getValueTimeUnit(), bloomFilter, filter, cacheGetIfAbsent);
    }

    /**
     * 首先，它尝试从缓存中获取键为 key 的值，并将其转换为 clazz 类型的对象，存储在 result 中。 <p>
     * 然后，它检查 result 是否为空或空字符串。如果不是，它将直接返回 result。此外，如果 cacheGetFilter 不为空并且 cacheGetFilter.filter(key) 返回 true，或者 bloomFilter 不为空并且 key 不在 bloomFilter 中，它也会直接返回 result。<p>
     * 如果以上条件都不满足，它将获取一个分布式锁，锁的键为 SAFE_GET_DISTRIBUTED_LOCK_KEY_PREFIX + key。  <p>
     * 在获取锁后，它再次尝试从缓存中获取键为 key 的值，并将其转换为 clazz 类型的对象，存储在 result 中。这是一个双重检查锁，用于减轻在获取分布式锁后访问数据库的压力。<p>
     * 如果 result 仍然为空，它将调用 cacheLoader.load() 方法加载数据，并将加载的数据存储在 result 中。如果加载的数据不为空，它将使用 loadAndSet 方法将数据存储在缓存中，并可能将 key 添加到 bloomFilter 中。<p>
     * 如果加载的数据仍然为空，它将执行 cacheGetIfAbsent.execute(key) 方法。  <p>
     * 最后，无论结果如何，它都会释放分布式锁，并返回 result。<p>
     * <p>
     * 这个方法的目的是确保在并发环境下，只有一个线程可以加载数据并更新缓存，从而避免缓存击穿问题。同时，它还使用了布隆过滤器来减少对不存在的键的查询，从而进一步提高性能。
     * @param key
     * @param clazz
     * @param cacheLoader
     * @param timeout
     * @param timeUnit
     * @param bloomFilter
     * @param cacheGetFilter
     * @param cacheGetIfAbsent
     * @return
     * @param <T>
     */
    @Override
    public <T> T saftGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheGetFilter, CacheGetIfAbsent<String> cacheGetIfAbsent) {
        T result = get(key, clazz);
        if (!CacheUtil.isNullOrBlank(result)
                || Optional.ofNullable(cacheGetFilter).map(each -> each.filter(key)).orElse(false)
                || Optional.ofNullable(bloomFilter).map(each -> !each.contains(key)).orElse(false)) {
            return result;
        }
        RLock lock = redissonClient.getLock(SAFE_GET_DISTRIBUTED_LOCK_KEY_PREFIX + key);
        lock.lock();
        try {
            // 双重判定锁：通过两次 if 判断减轻获得分布式锁后线程访问数据库压力
            if(CacheUtil.isNullOrBlank((result = get(key,clazz)))){
                if(CacheUtil.isNullOrBlank((result = loadAndSet(key,cacheLoader,timeout,timeUnit,true,bloomFilter)))){
                    Optional.ofNullable(cacheGetIfAbsent).ifPresent(each -> each.execute(key));
                }

            }
        }finally {
            lock.unlock();
        }
        return result;
    }


    @Override
    public void put(String key, Object value, long timeout) {
        put(key, value, timeout, redisProperties.getValueTimeUnit());
    }


    @Override
    public void put(String key, Object value, long timeout, TimeUnit timeUnit) {
        String actual = value instanceof String ? (String) value : JSON.toJSONString(value);
        stringRedisTemplate.opsForValue().set(key, actual, timeout, timeUnit);
    }


    @Override
    public void safePut(String key, Object value, long timeout, RBloomFilter<String> bloomFilter) {
        safePut(key, value, timeout, redisProperties.getValueTimeUnit(), bloomFilter);
    }


    @Override
    public void safePut(String key, Object value, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        put(key, value, timeout, timeUnit);
        if (bloomFilter != null) {
            bloomFilter.add(key);
        }
    }


    @Override
    public Long countExistingKeys(@NotNull String... keys) {
        return stringRedisTemplate.countExistingKeys(Lists.newArrayList(keys));
    }

    private <T> T loadAndSet(String key, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, boolean safeFlag, RBloomFilter<String> bloomFilter) {
        T result = cacheLoader.load();
        if (CacheUtil.isNullOrBlank(result)) {
            return result;
        }
        if (safeFlag) {
            safePut(key, result, timeout, timeUnit, bloomFilter);
        } else {
            put(key, result, timeout, timeUnit);
        }
        return result;
    }
}
