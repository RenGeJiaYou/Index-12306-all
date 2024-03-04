package com.sjj.idempotentspringbootstarter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 消息队列幂等自动配置类
 *
 * @author Island_World
 */
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {
}
