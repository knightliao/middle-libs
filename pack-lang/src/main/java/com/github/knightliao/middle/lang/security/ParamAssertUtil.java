package com.github.knightliao.middle.lang.security;

import com.github.knightliao.middle.lang.exceptions.BizException;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/4 15:28
 */
public class ParamAssertUtil {

    public static void assertArgumentValid(boolean condition, String message) {

        if (!condition) {
            throw BizException.getParamError(message, null);
        }
    }

    public static void assertArgumentNotNull(Object object, String field) {
        if (object == null) {
            throw BizException.getParamError(field, null);
        }
    }
}
