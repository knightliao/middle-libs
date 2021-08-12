package com.github.knightliao.middle.thread;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/11 16:19
 */
@Slf4j
public class MyThreadContext {

    private static final ThreadLocal<Map<String, Object>> CTX = new ThreadLocal<>();

    private static final String PRINT_LOG_KEY = "PRINT_LOG_KEY";

    private static final String ERROR_LOG_INFO = "调用ThreadContext时，必须要先进行ThreadContext的init，线程退出前再进行clean, "
            + "避免被其他线程使用到本线程的数据，发生线程安全问题";

    public MyThreadContext() {
    }

    public static void ensureInited() {
        if (CTX.get() == null) {
            init();
        }
    }

    public static void clean() {
        CTX.remove();
    }

    public static final boolean init() {
        if (CTX.get() != null) {
            return false;
        } else {

            Map<String, Object> currentThreadCtx = new HashMap<>(16);
            CTX.set(currentThreadCtx);
            return true;
        }
    }

    public static final <V> void put(String key, V value) {
        try {
            ((Map) CTX.get()).put(key, value);
        } catch (NullPointerException var3) {
            log.error(ERROR_LOG_INFO);
            throw var3;
        }
    }

    public static final <V> V get(String key) {
        Map<String, Object> map = CTX.get();
        return map == null ? null : (V) map.get(key);
    }

    public static void putPrintLogKey(boolean isPrintLog) {
        put(PRINT_LOG_KEY, isPrintLog);
    }

    public static void removePrintLogKey() {
        try {
            ((Map) CTX.get()).remove(PRINT_LOG_KEY);
        } catch (NullPointerException var3) {
            log.error(ERROR_LOG_INFO);
            throw var3;
        }
    }

    public static boolean isPrintLog() {
        Boolean ret = get(PRINT_LOG_KEY);

        if (ret != null && ret) {
            return true;
        }

        return false;
    }
}
