package com.sjj.cachespringbootstarter.config;

import com.sjj.cachespringbootstarter.RedisKeySerializer;
import com.sjj.cachespringbootstarter.StringRedisTemplateProxy;
import lombok.AllArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * cache 组件的自动配置
 *
 * @author Island_World
 */

@AllArgsConstructor
@EnableConfigurationProperties({RedisDistributedProperties.class,BloomFilterPenetrateProperties.class})
public class CacheAutoConfiguration {

    private final RedisDistributedProperties redisProperties;

    @Bean
    public RedisKeySerializer redisKeySerializer() {
        String prefix = redisProperties.getPrefix();
        String prefixCharset = redisProperties.getPrefixCharset();
        return new RedisKeySerializer(prefix, prefixCharset);
    }

    @Bean
    public RBloomFilter<String> cachePenetrationBloomFilter(RedissonClient redissonClient,BloomFilterPenetrateProperties bloomFilterPenetrateProperties){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterPenetrateProperties.getName());
        bloomFilter.tryInit(bloomFilterPenetrateProperties.getExpectedInsertions(),bloomFilterPenetrateProperties.getFalseProbability());
        return bloomFilter;
    }

    @Bean
    public StringRedisTemplateProxy stringRedisTemplateProxy(RedisKeySerializer serializer,
                                                             StringRedisTemplate stringRedisTemplate,
                                                             RedissonClient redissonClient){
        stringRedisTemplate.setKeySerializer(serializer);
        return new StringRedisTemplateProxy(stringRedisTemplate,redissonClient,redisProperties);
    }
}
