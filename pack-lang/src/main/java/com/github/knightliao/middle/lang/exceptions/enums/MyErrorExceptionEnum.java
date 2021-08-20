package com.github.knightliao.middle.lang.exceptions.enums;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:35
 */
@Getter
public enum MyErrorExceptionEnum {

    ILLEGAL_ARGUMENT(0, "ILLEGEAL_ARGUMENT"),
    NOT_FOUND(1, "NOT_FOUND");

    private final int value;
    private final String key;

    MyErrorExceptionEnum(int value, String key) {
        this.key = key;
        this.value = value;
    }

    public static MyErrorExceptionEnum getByValue(Integer input) {
        for (MyErrorExceptionEnum value : MyErrorExceptionEnum.values()) {
            if (value.getValue() == input) {
                return value;
            }
        }

        return null;
    }
}
