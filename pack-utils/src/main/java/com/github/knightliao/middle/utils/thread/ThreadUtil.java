package com.github.knightliao.middle.utils.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:17
 */
@Slf4j
public class ThreadUtil {

    public static void sleep(int mills) {

        try {

            Thread.sleep(mills);
        } catch (InterruptedException ex) {

            log.warn("sleep interrupted", ex);
        }
    }
}
