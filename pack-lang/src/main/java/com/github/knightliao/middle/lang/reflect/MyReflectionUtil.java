package com.github.knightliao.middle.lang.reflect;

import java.lang.reflect.Constructor;

import com.github.knightliao.middle.lang.exceptions.exceptions.others.SystemException;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/12 01:57
 */
public class MyReflectionUtil {

    public static <T> T newWithConstructor(String className) {

        try {

            Class<T> metaClass = (Class<T>) Class.forName(className);
            Constructor<T> con = metaClass.getConstructor();
            return con.newInstance();

        } catch (Exception ex) {

            throw new SystemException("new instance failed " + className, ex);
        }
    }
}
