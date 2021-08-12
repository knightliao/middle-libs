package com.github.knightliao.middle.idem.support.enums;

import lombok.Getter;

/**
 * @author knightliao
 * @date 2021/8/12 14:32
 */
@Getter
public enum IdemErrorEnum {

    DUPLICATION(0, "DUPLICATION"),
    CONCURRENT(1, "CONCURRENT"),
    BIZ_EXCEPTION(2, "BIZ_EXCEPTION");

    private final int value;
    private final String desc;

    IdemErrorEnum(int value, String desc) {
        this.desc = desc;
        this.value = value;
    }

    public static IdemErrorEnum getByValue(Integer input) {
        for (IdemErrorEnum value : IdemErrorEnum.values()) {
            if (value.getValue() == input) {
                return value;
            }
        }

        return null;
    }
}
