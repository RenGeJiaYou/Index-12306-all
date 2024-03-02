package com.sjj.distributedidspringbootstarter.config;

import com.sjj.basespringbootstarter.ApplicationContextHolder;
import com.sjj.distributedidspringbootstarter.core.snowflake.LocalRedisWorkIdChoose;
import com.sjj.distributedidspringbootstarter.core.snowflake.RandomWorkIdChoose;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 分布式 ID 配置类
 *
 * @author Island_World
 */

@Import(ApplicationContextHolder.class)
public class DistributedIdAutoConfiguration {
    /**
     * 本地 Redis 构建雪花 WorkID 标识码
     * */
    @Bean
    @ConditionalOnProperty("spring.data.redis.host")
    public LocalRedisWorkIdChoose redisWorkIdChoose() {
        return new LocalRedisWorkIdChoose();
    }

    /**
     * 随机数构建雪花码
     * */
    @Bean
    @ConditionalOnMissingBean(LocalRedisWorkIdChoose.class)
    public RandomWorkIdChoose randomWorkIdChoose() {
        return new RandomWorkIdChoose();
    }
}
