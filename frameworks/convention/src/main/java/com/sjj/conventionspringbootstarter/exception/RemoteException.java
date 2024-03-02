package com.sjj.conventionspringbootstarter.exception;

/*
 * 远程服务调用异常
 *
 * @author Island_World
 */

import com.sjj.conventionspringbootstarter.errorcode.BaseErrorCode;
import com.sjj.conventionspringbootstarter.errorcode.IErrorCode;

public class RemoteException extends AbstractException{
    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message,IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
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
