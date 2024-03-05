package com.sjj.idempotentspringbootstarter.config;

import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.idempotentspringbootstarter.core.IdempotentAspect;
import com.sjj.idempotentspringbootstarter.core.param.IdempotentParamExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.param.IdempotentParamService;
import com.sjj.idempotentspringbootstarter.core.spel.IdempotentSpELByMQExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.spel.IdempotentSpELService;
import com.sjj.idempotentspringbootstarter.core.token.IdempotentTokenExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.token.IdempotentTokenService;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 消息队列幂等自动配置类
 *
 * @author Island_World
 */
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {
    /**
     * 幂等切面
     */
    @Bean
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

    /**
     * 参数方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentParamService idempotentParamExecuteHandler(RedissonClient redissonClient) {
        return new IdempotentParamExecuteHandler(redissonClient);
    }

    /**
     * Token 方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentTokenService idempotentTokenExecuteHandler(DistributedCache distributedCache,
                                                                IdempotentProperties idempotentProperties) {
        return new IdempotentTokenExecuteHandler(distributedCache, idempotentProperties);
    }

    /**
     * SpEL 方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELService idempotentSpELByRestAPIExecuteHandler(RedissonClient redissonClient) {
        return new IdempotentSpELByRestAPIExecuteHandler(redissonClient);
    }

    /**
     * SpEL 方式幂等实现，基于 MQ 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELByMQExecuteHandler idempotentSpELByMQExecuteHandler(DistributedCache distributedCache) {
        return new IdempotentSpELByMQExecuteHandler(distributedCache);
    }
}
