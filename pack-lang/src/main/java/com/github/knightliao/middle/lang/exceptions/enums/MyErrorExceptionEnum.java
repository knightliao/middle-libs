package com.github.knightliao.middle.lang.exceptions.enums;

import java.util.Objects;

import lombok.Getter;

/**
 * 000 未知异常
 * 001 系统异常
 * 002 NPE
 * 010~099 网络异常
 * 100-149 请求参数检验异常
 * 150-199 程序控制异常
 *
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:35
 */
@Getter
public enum MyErrorExceptionEnum {

    UNKNOWN_ERROR("000", "UNKNOWN_ERROR"),
    SYSTEM_ERROR("001", "SYSTEM_ERROR"),
    NPE_ERROR("002", "NPE_ERROR"),

    NET_SOCKET_ERROR("010", "NET_SOCKET_ERROR"),

    PARAM_ERROR("100", "PARAM_ERROR"),
    PARAM_IDEM_DUPLICATION("101", "IDEM_DUPLICATION"),
    PARAM_IDEM_CONCURRENT("102", "IDEM_CONCURRENT"),

    BIZ_EXCEPTION("150", "BIZ_EXCEPTION");

    private final String key;
    private final String desc;

    MyErrorExceptionEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static MyErrorExceptionEnum getByValue(String key) {
        for (MyErrorExceptionEnum value : MyErrorExceptionEnum.values()) {
            if (Objects.equals(value.getKey(), key)) {
                return value;
            }
        }

        return null;
    }
}
