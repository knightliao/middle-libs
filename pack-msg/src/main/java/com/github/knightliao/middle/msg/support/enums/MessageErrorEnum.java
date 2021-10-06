package com.github.knightliao.middle.msg.support.enums;

import java.util.Objects;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:49
 */
@Getter
public enum MessageErrorEnum {

    NO_RETRY("NO_RETRY", 0),
    TYPE_ERROR("TYPE_ERROR", 1),
    RETRY("RETRY", 2);

    private final String key;
    private final int value;

    MessageErrorEnum(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public static MessageErrorEnum getByValue(String key) {
        for (MessageErrorEnum value :  MessageErrorEnum.values()) {
            if (Objects.equals(value.getKey(), key)) {
                return value;
            }
        }

        return null;
    }
}
