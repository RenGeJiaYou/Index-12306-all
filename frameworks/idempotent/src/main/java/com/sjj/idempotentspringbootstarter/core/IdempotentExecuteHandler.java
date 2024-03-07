package com.sjj.idempotentspringbootstarter.core;/*
 * @author: Island_World
 *
 */

import com.sjj.idempotentspringbootstarter.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 幂等执行处理器
 *
 * @author Island_World
 */

public interface IdempotentExecuteHandler {

    /**
     * 幂等处理逻辑
     *
     * @param wrapper 幂等参数包装器
     */
    void handler(IdempotentParamWrapper wrapper);

    /**
     * 执行幂等处理逻辑
     *
     * @param joinPoint 切入点
     * @param idempotent 幂等注解
     */
    void execute(ProceedingJoinPoint joinPoint, Idempotent idempotent);

    /**
     * 异常处理,需要删除幂等标识方便下次 RocketMQ 再次通过重试队列投递
     */
    default void exceptionProcessing(){}

    /**
     * 后置处理
     */
    default void postProcessing(){}
}
