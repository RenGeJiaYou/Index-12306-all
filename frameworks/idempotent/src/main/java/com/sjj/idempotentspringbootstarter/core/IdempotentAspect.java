package com.sjj.idempotentspringbootstarter.core;

import com.sjj.idempotentspringbootstarter.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
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
