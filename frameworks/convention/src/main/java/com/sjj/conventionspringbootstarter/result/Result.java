package com.sjj.conventionspringbootstarter.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/*
 * 统一返回结果类
 *
 * @Author Island_World
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    // 自定义一个 long 值,注意唯一性
    private static final long serialVersionUID = 5679018624309023727L;

    // 成功返回码
    public static final String SUCCESS_CODE = "0";

    // 返回消息
    private String code;

    // 响应消息
    private String message;

    // 响应数据
    private T data;

    // 请求 ID,方便链路追踪
    private String requestId;

    public boolean isSuccess(){
        return SUCCESS_CODE.equals(code);
    }
}
