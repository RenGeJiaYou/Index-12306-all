package com.sjj.conventionspringbootstarter.exception;

/*
 * 客户端异常
 *
 * @Author Island_World
 */

import com.sjj.conventionspringbootstarter.errorcode.BaseErrorCode;
import com.sjj.conventionspringbootstarter.errorcode.IErrorCode;

public class ClientException extends AbstractException{
    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message){
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message,IErrorCode errorCode){
        this(message, null, errorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{"+
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                "}";
    }
}
