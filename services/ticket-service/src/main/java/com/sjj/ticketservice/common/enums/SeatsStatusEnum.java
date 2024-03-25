package com.sjj.ticketservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 座位状态枚举
 *
 * @author Island_World
 */

@Getter
@RequiredArgsConstructor
public enum SeatsStatusEnum {
    /**
     * 可售
     */
    AVAILABLE(0),

    /**
     * 锁定
     */
    LOCKED(1),

    /**
     * 已售
     */
    SOLD(2);

    private final Integer code;
}
