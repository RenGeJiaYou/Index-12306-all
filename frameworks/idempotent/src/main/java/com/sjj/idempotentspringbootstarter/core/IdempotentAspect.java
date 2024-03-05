package com.sjj.idempotentspringbootstarter.core;

import com.sjj.idempotentspringbootstarter.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 幂等注解 AOP 拦截器
 *
 * @author Island_World
 */

@Aspect
public final class IdempotentAspect {

    /**
     * 当 {@link Idempotent} 注解标记在方法上时，起到增强效果
     */
    @Around("@annotation(com.sjj.idempotentspringbootstarter.annotation.Idempotent)")
    public Object idempotentHandler(ProceedingJoinPoint joinPoint)throws Throwable{
        Idempotent idempotent = getIdempotent(joinPoint);
        IdempotentExecuteHandler handler = IdempotentExecuteHandlerFactory.getInstance(idempotent.scene(), idempotent.type());
        Object resultObj;
        try{
            handler.execute(joinPoint,idempotent);
            resultObj = joinPoint.proceed();
            handler.postProcessing();
        }catch (RepeatConsumptionException ex){
            /**
             * 触发幂等逻辑时可能有两种情况：
             *    * 1. 消息还在处理，但是不确定是否执行成功，那么需要返回错误，方便 RocketMQ 再次通过重试队列投递
             *    * 2. 消息处理成功了，该消息直接返回成功即可
             */
            if (!ex.getError()) {
                return null;
            }
            throw ex;
        }catch (Throwable e){
            // 客户端消费存在异常，需要删除幂等标识方便下次 RocketMQ 再次通过重试队列投递
            handler.exceptionProcessing();
            throw e;
        }
        finally{
            IdempotentContext.clean();
        }
        return resultObj;
    }

    /**
     * 获取切入点，即被代理方法上的注解实例，获得传入注解的实参
     * */
    public static Idempotent getIdempotent(ProceedingJoinPoint joinPoint) throws NoSuchMethodException{
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(Idempotent.class);
    }
}
