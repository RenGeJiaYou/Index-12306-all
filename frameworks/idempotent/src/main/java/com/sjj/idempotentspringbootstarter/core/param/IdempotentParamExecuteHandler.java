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

package com.sjj.idempotentspringbootstarter.core.param;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.sjj.conventionspringbootstarter.exception.ClientException;
import com.sjj.idempotentspringbootstarter.core.AbstractIdempotentExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.IdempotentContext;
import com.sjj.idempotentspringbootstarter.core.IdempotentParamWrapper;
import com.sjj.userspringbootstarter.core.UserContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基于方法参数验证幂等性
 *
 * @author Island_World
 */

// @RequiredArgsConstructor 生成一个包含所有未初始化的 final 字段和 @NonNull 字段的构造函数。
@RequiredArgsConstructor
public class IdempotentParamExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentParamService {
    private final RedissonClient redissonClient;

    private final String Lock = "lock:param:restAPI";

    /**
     * @return 获取当前线程上下文 ServletPath
     * <p>
     * 该部分指向 Servlet 的路径。这个路径是相对于当前 Servlet 上下文的路径，不包括任何额外的路径信息或查询字符串。<p>
     * 例如，如果 URL 是 http://localhost:8080/myapp/myservlet`，那么getServletPath()将返回 `/myservlet`。
     */
    private String getServletPath() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest().getServletPath();
    }

    /**
     * @return 当前操作用户 ID
     */
    private String getCurrentUserId() {
        String userId = UserContext.getUserId();
        if (StrUtil.isBlank(userId)) {
            throw new ClientException("用户ID获取失败，请登录");
        }
        return userId;
    }

    /**
     * @Return 被注解方法的形参列表的 MD5 值
     */
    private String calcArgsMD5(ProceedingJoinPoint joinPoint) {
        return DigestUtil.md5Hex(JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * @Return 构造幂等验证过程中所需要的参数包装器
     */
    @Override
    public IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        String lockKey = String.format("idempotent:path:%s:currentUserId:%s:md5:%s",
                getServletPath(),
                getCurrentUserId(),
                calcArgsMD5(joinPoint));

        return IdempotentParamWrapper.builder().lockKey(lockKey).joinPoint(joinPoint).build();
    }

    /**
     * handler() 中加可重入锁
     */
    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        RLock lock = redissonClient.getLock(wrapper.getLockKey());
        if (!lock.tryLock()) {
            throw new ClientException(wrapper.getIdempotent().message());
        }
        IdempotentContext.put(Lock, lock);
    }


    /**
     * postProcessing() 中释放可重入锁
     */
    @Override
    public void postProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContext.getKey(Lock);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }
}
