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

package com.sjj.webspringbootstarter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.sjj.conventionspringbootstarter.errorcode.BaseErrorCode;
import com.sjj.conventionspringbootstarter.exception.AbstractException;
import com.sjj.conventionspringbootstarter.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 使用 @RestControllerAdvice 的就是全局异常处理类
 *
 * @author Island_World
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private String getUrl(HttpServletRequest request){
        if (StrUtil.isEmpty(request.getQueryString())){
            return request.getRequestURL().toString();
        }
        return request.getRequestURL() + "?" + request.getQueryString();
    }

    /**
     * 拦截参数验证异常
     *
     * @param request
     * @param ex
     * @return 带有错误码和校验失败消息的失败响应 Result 对象
     */
    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex){
        // 包含了所有的验证错误信息
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        String errMsg = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);

        log.error("[{}] {} [Valid Exception] {}",
                request.getMethod(),
                getUrl(request),
                errMsg);

        return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), errMsg);
    }

    @ExceptionHandler(value = AbstractException.class)
    public Result abstractException(HttpServletRequest request, AbstractException ex){
        if(ex.getCause()!= null){
            log.error("[{}] {} [Abstract Exception] {}",
                    request.getMethod(),
                    request.getRequestURL().toString(),
                    ex,
                    ex.getCause());
        }
        log.error("[{}] {} [ex] {}",
                request.getMethod(),
                request.getRequestURL().toString(),
                ex.toString());
        return Results.failure(ex);
    }

    @ExceptionHandler(value = Throwable.class)
    public Result defaultErrorHandler(HttpServletRequest request,Throwable throwable){
        log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
        return Results.failure();
    }
}
