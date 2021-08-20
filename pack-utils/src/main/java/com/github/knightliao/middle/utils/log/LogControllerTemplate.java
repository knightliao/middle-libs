package com.github.knightliao.middle.utils.log;

import com.google.common.util.concurrent.RateLimiter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/19 21:10
 */
@Slf4j
public class LogControllerTemplate {

    private static RateLimiter rateLimiter = RateLimiter.create(100);

    private static boolean printLog(int times) {

        rateLimiter.setRate(times);
        return rateLimiter.tryAcquire();
    }

    public static void doFewLog(int times, Runnable callback) {
        try {
            if (printLog(times)) {
                callback.run();
            }
        } catch (Throwable throwable) {
            log.error("处理回调失败", throwable);
        }
    }

    public static void doFewLog(Runnable callback) {
        doFewLog(200, callback);
    }
}
