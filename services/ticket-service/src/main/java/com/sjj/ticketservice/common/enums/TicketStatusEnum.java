package com.sjj.ticketservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 车票状态枚举
 *
 * @author Island_World
 */

@Getter
@RequiredArgsConstructor
public enum TicketStatusEnum {
    /**
     * 未支付
     */
    UNPAID(0),

    /**
     * 已支付
     */
    PAID(1),

    /**
     * 改签
     */
    CHANGED(2),

    /**
     * 退票
     */
    REFUNDED(3);

    private final Integer code;
}
