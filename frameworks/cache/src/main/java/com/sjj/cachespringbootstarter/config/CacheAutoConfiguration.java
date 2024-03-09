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
