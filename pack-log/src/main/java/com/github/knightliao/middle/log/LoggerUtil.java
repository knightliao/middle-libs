package com.github.knightliao.middle.log;

import java.text.MessageFormat;

import org.slf4j.Logger;

import com.github.knightliao.middle.lang.templates.NoErrorTemplate;
import com.github.knightliao.middle.thread.MyThreadContext;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/6 21:08
 */
public class LoggerUtil {

    public static void debug(Logger logger, String template, Object... parameters) {

        if (logger.isDebugEnabled()) {
            NoErrorTemplate.handle(() -> {
                logger.debug(render(template, parameters));
            });
        }
    }

    public static void info(Logger logger, String template, Object... parameters) {

        if (logger.isInfoEnabled()) {
            NoErrorTemplate.handle(() -> {
                logger.info(render(template, parameters));
            });
        }
    }

    public static void warn(Logger logger, String template, Object... parameters) {

        if (logger.isInfoEnabled()) {
            NoErrorTemplate.handle(() -> {
                logger.warn(render(template, parameters));
            });
        }
    }

    // 精细化打日志
    public static void infoIfNeed(Logger logger, String template, Object... parameters) {

        if (logger.isInfoEnabled() && MyThreadContext.isPrintLog()) {
            NoErrorTemplate.handle(() -> {
                logger.info(render(template, parameters));
            });
        }
    }

    public static void error(Throwable e, Logger logger, String template, Object... parameters) {

        NoErrorTemplate.handle(() -> {
            logger.error(render(template, parameters), e);
        });
    }

    public static void error(Logger logger, String template, Object... parameters) {

        NoErrorTemplate.handle(() -> {
            logger.error(render(template, parameters));
        });
    }

    private static String render(String tpl, Object... params) {

        return params != null && params.length != 0 ? MessageFormat.format(tpl, numberToString(params)) : tpl;
    }

    private static Object[] numberToString(Object[] params) {

        for (int i = 0; i < params.length; ++i) {

            if (params[i] instanceof Number) {
                params[i] = String.valueOf(params[i]);
            }
        }

        return params;
    }
}
