package com.sjj.ticketservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 交通工具座位类型
 *
 * @author Island_World
 */

@Getter
@RequiredArgsConstructor
public enum VehicleSeatTypeEnum {
    /**
     * 商务座
     */
    BUSINESS_CLASS(0, "BUSINESS_CLASS"),

    /**
     * 一等座
     */
    FIRST_CLASS(1, "FIRST_CLASS"),

    /**
     * 二等座
     */
    SECOND_CLASS(2, "SECOND_CLASS");

    private final Integer code;
    private final String name;

    /**
     * 根据编码查找名称
     */
    public static String findNameByCode(Integer code){
        return Arrays.stream(VehicleSeatTypeEnum.values())
                .filter(each -> Objects.equals(code,each.getCode()))
                .findFirst()
                .map(VehicleSeatTypeEnum::getName)
                .orElse(null);
    }
}
