package com.github.knightliao.middle.lang.security;

import com.github.knightliao.middle.lang.exceptions.exceptions.param.ParamException;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 11:37
 */
public class MyAssertUtils {

    public static void assertTrue(boolean condition, String desc) {
        if (!condition) {
            throw new ParamException(desc);
        }
    }

    public static void assertNotNull(Object obj, String desc) {
        if (obj == null) {
            throw new ParamException(desc);
        }
    }

}
