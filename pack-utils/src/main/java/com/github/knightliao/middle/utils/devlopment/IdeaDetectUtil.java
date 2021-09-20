package com.github.knightliao.middle.utils.devlopment;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/20 11:27
 */
public class IdeaDetectUtil {

    private static final boolean IS_IDEA_ENV;

    public static boolean isInIDea() {
        return IS_IDEA_ENV;
    }

    static {
        IS_IDEA_ENV = detectIDeaEnv();
    }

    private static boolean detectIDeaEnv() {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader instanceof URLClassLoader) {

            final URL[] ursl = ((URLClassLoader) classLoader).getURLs();

            for (int i = 0; i < ursl.length; ++i) {
                URL url = ursl[i];
                if (url.toString().endsWith("idea_rt.jar")) {
                    return true;
                }
            }
        }

        return false;
    }
}
