package com.sjj.commonspringbootstarter.enums;

/**
 * 状态枚举
 *
 * @Author Island_World
 */

public enum StatusEnum {

    SUCCESS(0),
    FAIL(1);

    private final Integer statusCode;

    StatusEnum(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer code() {
        return this.statusCode;
    }

    public String strCode() {
        return String.valueOf(this.statusCode);
    }

    @Override
    public String toString() {
        return strCode();
    }
}
