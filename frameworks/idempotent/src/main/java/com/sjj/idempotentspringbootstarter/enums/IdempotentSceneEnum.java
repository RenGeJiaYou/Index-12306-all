package com.sjj.idempotentspringbootstarter.enums;

/**
 * 幂等验证场景枚举
 *
 * @Author: Island_World
 *
 */

public enum IdempotentSceneEnum {
    // 基于 RestAPI 场景验证
    RESTAPI,

    // 基于 MQ 场景验证
    MQ
}
