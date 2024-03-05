package com.sjj.idempotentspringbootstarter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 幂等 MQ 消费状态枚举
 *
 * @author: Island_World
 *
 */

@RequiredArgsConstructor
public enum IdempotentMQConsumeStatusEnum {
    // 消费中
    CONSUMING("0"),

    // 消费成功
    CONSUMES("1");

    @Getter
    private final String code;

    /**
     * 如果消费状态等于消费中，返回失败
     *
     * @param consumeStatus 消费状态
     * @return 是否消费失败
     */
    public static boolean isError(String consumeStatus) {
        return CONSUMING.getCode().equals(consumeStatus);
    }
}
