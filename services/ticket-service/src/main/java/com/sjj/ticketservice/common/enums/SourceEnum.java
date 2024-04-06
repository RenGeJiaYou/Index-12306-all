package com.sjj.ticketservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 购票来源枚举类
 *
 * @author Island_World
 */
@Getter
@RequiredArgsConstructor
public enum SourceEnum {
    /**
     * 互联网购票
     */
    ONLINE(0),

    /**
     * 线下窗口购票
     */
    OFFLINE(1);

    private final Integer code;
}
