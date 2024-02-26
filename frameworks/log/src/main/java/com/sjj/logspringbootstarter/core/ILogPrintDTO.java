package com.sjj.logspringbootstarter.core;

import lombok.Data;

/**
 * 日志打印实体类
 *
 * @Author Island_World
 */

@Data
public class ILogPrintDTO {
    // 开始时间
    private String beginTime;

    // 请求入参
    private Object[] inputParams;

    // 返回参数
    private Object outputParams;
}
