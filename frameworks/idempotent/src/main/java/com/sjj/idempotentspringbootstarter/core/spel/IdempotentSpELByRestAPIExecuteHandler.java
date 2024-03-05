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
public class IdempotentSpELByRestAPIExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentSpELService{
    private final RedissonClient redisson;
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
                ((MethodSignature)joinPoint.getSignature()).getMethod(),
                joinPoint.getArgs());
        // 无需加入 idempotent 是由于 joinPoint 将提供该值
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
        RLock lock = redisson.getLock(uniqueKey);// 获取锁
        if(!lock.tryLock()){
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
        try{
            lock = (RLock) IdempotentContext.getKey(LOCK);
        }finally {
            if (lock!=null){
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
        try{
            lock = (RLock) IdempotentContext.getKey(LOCK);
        }finally {
            if (lock!=null){
                lock.unlock();
            }
        }
    }
}
