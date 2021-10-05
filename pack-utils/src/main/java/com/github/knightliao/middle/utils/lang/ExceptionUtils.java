package com.github.knightliao.middle.utils.lang;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/5 18:04
 */
public class ExceptionUtils {

    public static boolean exceptionMatch(Throwable throwable, Class<? extends Throwable> clazz) {

        if (clazz.isInstance(throwable)) {
            return true;
        }

        while ((throwable = throwable.getCause()) != null) {

            if (clazz.isInstance(throwable)) {
                return true;
            }
        }

        return false;
    }
}
