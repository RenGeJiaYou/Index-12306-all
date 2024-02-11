package com.sjj.conventionspringbootstarter.exception;

/*
 * 异常处理抽象类，抽象项目中三大类异常体系
 *  客户端异常、
 *  服务端异常
 *  远程服务调用异常
 *
 * @Author Island_World
 */

import com.sjj.conventionspringbootstarter.errorcode.IErrorCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Getter
public abstract class AbstractException extends RuntimeException {
    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message,throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional
                .ofNullable(StringUtils.hasLength(message)?message:null)
                .orElse(errorCode.message());
    }

}
