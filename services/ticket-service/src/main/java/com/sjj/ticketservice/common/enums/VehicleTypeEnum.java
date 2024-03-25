package com.sjj.ticketservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 交通工具类型
 *
 * @author Island_World
 */

@Getter
@RequiredArgsConstructor
public enum VehicleTypeEnum {
    /**
     * 高铁
     */
    HIGH_SPEED_RAIN(0, "HIGH_SPEED_RAIN"),

    /**
     * 火车
     */
    TRAIN(1, "TRAIN"),

    /**
     * 汽车
     */
    CAR(2, "CAR"),

    /**
     * 飞机
     */
    AIRPLANE(3, "AIRPLANE");

    private final Integer code;
    private final String name;

    /**
     * 根据编码查找名称
     */
    public static String findNameByCode(Integer code){
        return Arrays.stream(VehicleTypeEnum.values())
                .filter(each -> Objects.equals(code,each.getCode()))
                .findFirst()
                .map(VehicleTypeEnum::getName)
                .orElse(null);
    }
}
