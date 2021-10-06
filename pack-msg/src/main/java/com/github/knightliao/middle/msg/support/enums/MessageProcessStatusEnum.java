package com.github.knightliao.middle.msg.support.enums;

import java.util.Objects;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:41
 */
@Getter
public enum MessageProcessStatusEnum {

    NORMAL("NORMAL", 0),
    RETRY("RETRY", 1),
    IGNORE("IGNORE", 2),
    ERROR("ERROR", 3),
    DECODE_ERROR("DECODE_ERROR", 4);

    private final String key;
    private final int value;

    MessageProcessStatusEnum(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public static MessageProcessStatusEnum getByValue(String key) {
        for (MessageProcessStatusEnum value : MessageProcessStatusEnum.values()) {
            if (Objects.equals(value.getKey(), key)) {
                return value;
            }
        }

        return null;
    }
}
