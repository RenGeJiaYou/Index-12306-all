package com.sjj.ticketservice.common.enums;

import cn.hutool.core.collection.ListUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 地区&站点类型枚举
 *
 * @author Island_World
 */
@RequiredArgsConstructor
public enum RegionStationQueryTypeEnum {
    HOT(0,null),


    /**
     * A to E
     */
    A_E(1, ListUtil.of("A", "B", "C", "D", "E")),

    /**
     * F to J
     */
    F_J(2, ListUtil.of("F", "G", "H", "I", "J")),

    /**
     * K to O
     */
    K_O(3, ListUtil.of("K", "L", "M", "N", "O")),

    /**
     * P to T
     */
    P_T(4, ListUtil.of("P", "Q", "R", "S", "T")),

    /**
     * U to Z
     */
    U_Z(5, ListUtil.of("U", "V", "W", "X", "Y", "Z"));
    /**
     * 类型
     */
    @Getter
    private final Integer type;

    /**
     * 拼音列表
     */
    @Getter
    private final List<String> spells;

    public static List<String> findSpellsByType(Integer type){
        return Arrays.stream(RegionStationQueryTypeEnum.values())
                .filter(each -> Objects.equals(each.getType(),type))
                .findFirst()
                .map(RegionStationQueryTypeEnum::getSpells)
                .orElse(null);
    }
}
