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
