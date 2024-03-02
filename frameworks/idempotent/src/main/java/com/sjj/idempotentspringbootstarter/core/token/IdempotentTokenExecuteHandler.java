package com.sjj.idempotentspringbootstarter.core.token;

import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.conventionspringbootstarter.exception.ClientException;
import com.sjj.idempotentspringbootstarter.core.AbstractIdempotentExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.IdempotentContext;
import com.sjj.idempotentspringbootstarter.core.IdempotentParamWrapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;

/**
 * 基于 Token 验证幂等性,通常应用于 RestAPI 方法
 *
 * @author Island_World
 */

// @RequiredArgsConstructor 生成一个包含所有未初始化的 final 字段和 @NonNull 字段的构造函数。
@RequiredArgsConstructor
public class IdempotentTokenExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentTokenService {
    private final DistributedCache distributedCache;








    /**
     * @Return 构造幂等验证过程中所需要的参数包装器
     * */
    @Override
    public IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {


    }

    /**
     * handler() 中加可重入锁后，将锁放入上下文
     * */
    @Override
    public void handler(IdempotentParamWrapper wrapper) {

    }


    @Override
    public String createToken() {
        return null;
    }
}
