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

package com.sjj.idempotentspringbootstarter.core.spel;

import com.sjj.idempotentspringbootstarter.annotation.Idempotent;
import com.sjj.idempotentspringbootstarter.core.AbstractIdempotentExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.IdempotentAspect;
import com.sjj.idempotentspringbootstarter.core.IdempotentContext;
import com.sjj.idempotentspringbootstarter.core.IdempotentParamWrapper;
import com.sjj.idempotentspringbootstarter.toolkit.SpELUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 基于 SpEL 方法验证请求幂等性，适用于 RestAPI 场景
 *
 * @author Island_World
 */
@RequiredArgsConstructor
public class IdempotentSpELByRestAPIExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentSpELService {
    private final RedissonClient redissonClient;
    private final static String LOCK = "lock:spEL:restAPI";


    /**
     * 构建幂等验证过程中所需要的参数包装器
     *
     * @param joinPoint AOP 方法处理
     * @return 幂等参数包装器
     */
    @SneakyThrows
    @Override
    public IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        Idempotent idempotent = IdempotentAspect.getIdempotent(joinPoint);
        String key = (String) SpELUtil.parseKey(
                idempotent.key(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(),
                joinPoint.getArgs());
        // 无需加入 idempotent 是由于抽象类的 execute() 将最后完成这一步
        return IdempotentParamWrapper.builder().lockKey(key).joinPoint(joinPoint).build();
    }

    /**
     * 幂等处理逻辑
     *
     * @param wrapper 幂等参数包装器
     */
    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        String uniqueKey = wrapper.getIdempotent().uniqueKeyPrefix() + wrapper.getLockKey(); // 生成唯一键
        RLock lock = redissonClient.getLock(uniqueKey);// 获取锁
        if (!lock.tryLock()) {
            throw new RuntimeException(wrapper.getIdempotent().message());
        }
        IdempotentContext.put(LOCK, lock);
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContext.getKey(LOCK);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    /**
     * 后置处理,解锁
     */
    @Override
    public void postProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContext.getKey(LOCK);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }
}
