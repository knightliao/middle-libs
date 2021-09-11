package com.github.knightliao.middle.lang.security;

import com.github.knightliao.middle.lang.exceptions.exceptions.biz.BizException;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/4 15:28
 */
public class BizParamAssertUtil {

    public static void assertArgumentValid(boolean condition, String message) {

        if (!condition) {
            throw BizException.getParamError("", message);
        }
    }

    public static void assertArgumentNotNull(Object object, String field) {
        if (object == null) {
            throw BizException.getParamError(field);
        }
    }
}
