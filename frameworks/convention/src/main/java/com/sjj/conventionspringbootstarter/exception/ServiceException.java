package com.sjj.conventionspringbootstarter.exception;

/*
 * 服务端异常
 *
 * @Author Island_World
 */

import com.sjj.conventionspringbootstarter.errorcode.BaseErrorCode;
import com.sjj.conventionspringbootstarter.errorcode.IErrorCode;

public class ServiceException extends AbstractException{
    public ServiceException(String message) {
        this(message,null, BaseErrorCode.SERVICE_ERROR);
    }

    public ServiceException(IErrorCode errorCode){
        this(null, errorCode);
    }

    public ServiceException(String message, IErrorCode errorCode) {
        super(message, null, errorCode);
    }

    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{"+
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                "}";
    }
}
