package com.sjj.commonspringbootstarter.enums;

/**
 * 删除标记枚举
 *
 * @Author Island_World
 */

public enum FlagEnum {
    FALSE(0), TRUE(1);

    private final Integer flag;

    FlagEnum(Integer flag) {
        this.flag = flag;
    }

    public Integer code() {
        return this.flag;
    }

    public String strCode() {
        return String.valueOf(this.flag);
    }

    @Override
    public String toString() {
        return strCode();
    }
}
