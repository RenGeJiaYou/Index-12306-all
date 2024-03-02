package com.sjj.idempotentspringbootstarter.core;

import com.sjj.idempotentspringbootstarter.annotation.Idempotent;
import com.sjj.idempotentspringbootstarter.enums.IdempotentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 幂等参数包装
 *
 * @author Island_World
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IdempotentParamWrapper {
    // 幂等注解类
    private Idempotent idempotent;

    // 切入点
    private ProceedingJoinPoint joinPoint;

    /**
     * 锁标识，{@link IdempotentTypeEnum#PARAM}
     */
    private String lockKey;
}
