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
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

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
    public Boolean delete(String key) {return stringRedisTemplate.delete(key);}


    @Override
    public Long delete(Collection<String> keys) {return stringRedisTemplate.delete(keys);}


    @Override
    public Boolean hasKey(String key) {return stringRedisTemplate.hasKey(key);}


    @Override
    public Object getInstance() {return stringRedisTemplate;}

    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {

        return null;
    }


    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        T result = get(key, clazz);
        if(!CacheUtil.isNullOrBlank(result)){
            return result;
        }
        return loadAndSet(key, cacheLoader, timeout, timeUnit, false, null);
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
        put(key,value,timeout,redisProperties.getValueTimeUnit());
    }


    @Override
    public void put(String key, Object value, long timeout, TimeUnit timeUnit) {
        String actual = value instanceof String? (String)value:JSON.toJSONString(value);
        stringRedisTemplate.opsForValue().set(key,actual,timeout,timeUnit);
    }


    @Override
    public void safePut(String key, Object value, long timeout, RBloomFilter<String> bloomFilter) {
        safePut(key,value,timeout,redisProperties.getValueTimeUnit(),bloomFilter);
    }


    @Override
    public void safePut(String key, Object value, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        put(key,value,timeout,timeUnit);
        if (bloomFilter != null){
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
