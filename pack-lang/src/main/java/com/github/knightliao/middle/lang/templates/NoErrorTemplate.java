package com.github.knightliao.middle.lang.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/6 21:08
 */
public class NoErrorTemplate {

    private static final Logger log = LoggerFactory.getLogger(NoErrorTemplate.class);

    public NoErrorTemplate() {

    }

    public static void handle(Runnable callback) {
        try {
            callback.run();
        } catch (Throwable throwable) {
            log.error("处理回调失败", throwable);
        }
    }
}
