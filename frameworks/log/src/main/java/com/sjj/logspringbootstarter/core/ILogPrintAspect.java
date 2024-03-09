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

package com.sjj.logspringbootstarter.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import com.alibaba.fastjson2.JSON;
import com.sjj.logspringbootstarter.annotation.ILog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 日志打印 AOP 切面
 *
 * @within(com.sjj.logspringbootstarter.annotation.ILog) 表示所有在类级别上标注了 @ILog 注解的类中的所有方法都会被通知。
 * @annotation(com.sjj.logspringbootstarter.annotation.ILog) 表示所有在方法级别上标注了 @ILog 注解的方法都会被通知。
 * @author Island_World
 */
@Aspect("@within(com.sjj.logspringbootstarter.annotation.ILog) || @annotation(com.sjj.logspringbootstarter.annotation.ILog)")
public class ILogPrintAspect {
    public Object printMLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = SystemClock.now();
        // 获取连接点的方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 创建目标类的 Logger
        Logger log = LoggerFactory.getLogger(signature.getDeclaringType());
        String beginTime = DateUtil.now();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(
                    signature.getName(), signature.getMethod().getParameterTypes());
            // 优先获取方法上的注解，没有再获取类上的注解
            ILog logAnnotation = Optional.ofNullable(targetMethod.getAnnotation(ILog.class))
                    .orElse(joinPoint.getTarget().getClass().getAnnotation(ILog.class));

            if (logAnnotation != null) {
                ILogPrintDTO logPrintDTO = new ILogPrintDTO();
                logPrintDTO.setBeginTime(beginTime);
                if (logAnnotation.input()) {
                    logPrintDTO.setInputParams(buildInput(joinPoint));
                }
                if (logAnnotation.output()) {
                    logPrintDTO.setOutputParams(result);
                }
                String methodType = "", requestURI = "";
                try{
                    ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                    assert requestAttributes!=null;
                    // 保存 HTTP 请求方法，如 GET、POST 等
                    methodType = requestAttributes.getRequest().getMethod();
                    requestURI = requestAttributes.getRequest().getRequestURI();
                }catch (Exception ignored){
                }
                log.info("请求地址：{}，请求方式：{}，开始时间：{}，耗时：{}ms，请求参数：{}，返回参数：{}",
                        requestURI, methodType, beginTime, SystemClock.now() - startTime, JSON.toJSONString(logPrintDTO.getInputParams()), JSON.toJSONString(logPrintDTO.getOutputParams()));
            }
        }
        return result;
    }

    /**
     * This method is used to build the input parameters for the logging aspect.<p>
     * It takes a ProceedingJoinPoint as an argument which represents the current join point in the execution of the program.<p>
     * The method iterates over the arguments of the join point, and processes them based on their type.<p>
     * If the argument is an instance of HttpServletRequest or HttpServletResponse, it is ignored.<p>
     * If the argument is a byte array, it is replaced with the string "byte array".<p>
     * If the argument is an instance of MultipartFile (representing an uploaded file in an HTTP request), it is replaced with the string "file".<p>
     * All other types of arguments are left as they are.<p>
     * The processed arguments are then returned as an array.<p>
     * <p>
     * @param joinPoint the current join point in the execution of the program
     * @return an array of the processed arguments of the join point
     */
    private Object[] buildInput(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] printArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            // 忽略参数中的 HTTP 响应/请求报文
            if ((args[i] instanceof HttpServletRequest) || args[i] instanceof HttpServletResponse) {
                continue;
            }
            // 标记参数中的 byte[] 为 "byte array"
            if (args[i] instanceof byte[]) {
                printArgs[i] = "byte array";
            } else if (args[i] instanceof MultipartFile) {
                // 标记参数中的 MultipartFile 为 "file"
                printArgs[i] = "file";
            } else {
                printArgs[i] = args[i];
            }
        }
        return printArgs;
    }
}
