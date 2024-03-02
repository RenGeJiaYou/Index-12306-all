package com.sjj.cachespringbootstarter.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * cache 组件的自动配置
 *
 * @author Island_World
 */

@AllArgsConstructor
@EnableConfigurationProperties({RedisDistributedProperties.class,BloomFilterPenetrateProperties.class})
public class CacheAutoConfiguration {
}
