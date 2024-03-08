package com.sjj.webspringbootstarter;

/*
 * 构造指定类型的 Result 对象
 *
 * @author Island_World
 */

import com.sjj.conventionspringbootstarter.errorcode.BaseErrorCode;
import com.sjj.conventionspringbootstarter.exception.AbstractException;
import com.sjj.conventionspringbootstarter.result.Result;

import java.util.Optional;

public class Results {

    /**
     * 构造不带数据的成功响应
     *
     * @return Result<Void>
     */
    public static Result<Void> success() {
        // 将类的静态字段值赋给类的成员变量
        return new Result<Void>().setCode(Result.SUCCESS_CODE);
    }

    /**
     * 构造带数据的成功响应
     *
     * @param data
     * @return Result<T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>().setCode(Result.SUCCESS_CODE).setData(data);
    }

    /**
     * 构建服务器的失败响应
     *
     * @return Result<void>
     */
    protected static Result<Void> failure() {
        return new Result<Void>()
                .setCode(BaseErrorCode.SERVICE_ERROR.code())
                .setMessage(BaseErrorCode.SERVICE_ERROR.message());
    }

    /**
     * 通过 {@link AbstractException} 构建失败响应
     *
     * @param abstractException
     * @return
     */
    protected static Result<Void> failure(AbstractException abstractException) {
        String errorCode = Optional
                .ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional
                .ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());

        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    /**
     * 通过 errorCode、errorMessage 构建失败响应
     *
     * @param errorCode
     * @param errorMessage
     * @return
     */
    protected static Result<Void> failure(String errorCode, String errorMessage) {
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

}
