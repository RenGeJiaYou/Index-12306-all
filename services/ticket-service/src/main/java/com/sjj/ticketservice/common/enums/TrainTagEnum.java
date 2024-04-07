package com.sjj.ticketservice.common.enums;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 列车标签枚举
 *
 * @author Island_World
 */

@RequiredArgsConstructor
@Getter
public enum TrainTagEnum {

    FU_XING("0", "复兴号"),

    HIGH_SPEED_TRAIN("1", "GC-高铁/城际");

    private final String code;

    private final String name;

    /**
     * 根据编码查找名称
     */
    public static String findNameByCode(String code) {
        return Arrays.stream(TrainTagEnum.values())
                .filter(each -> Objects.equal(code, each.getCode()))
                .findFirst()
                .map(TrainTagEnum::getName)
                .orElse(null);
    }

    /**
     * 根据编码集合查找名称
     */
    public static List<String> findNameByCode(List<String> codes) {
        List<String> resultList = new ArrayList<>();
        for (String code : codes) {
            String name = Arrays.stream(TrainTagEnum.values())
                    .filter(each -> Objects.equal(code, each.getCode()))
                    .findFirst()
                    .map(TrainTagEnum::getName)
                    .orElse(null);
            if (StrUtil.isNotBlank(name)){
                resultList.add(name);
            }
        }
        return resultList;
    }
}
