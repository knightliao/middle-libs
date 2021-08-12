package com.github.knightliao.middle.lang.security;

import com.github.knightliao.middle.lang.exceptions.AssertException;
import com.github.knightliao.middle.lang.exceptions.NpeException;

/**
 * @author knightliao
 * @date 2021/8/4 15:28
 */
public class ParamAssertUtil {

    public static void assertArgumentValid(boolean condition, String message) {

        if (!condition) {
            throw new AssertException(message);
        }
    }

    public static void assertArgumentNotNull(Object object, String field) {
        if (object == null) {
            throw new NpeException(field);
        }
    }
}
