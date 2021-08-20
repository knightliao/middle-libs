package com.github.knightliao.middle.metrics;

import com.alibaba.metrics.FastCompass;
import com.alibaba.metrics.MetricLevel;
import com.alibaba.metrics.MetricManager;
import com.alibaba.metrics.MetricName;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/17 17:40
 */
@Slf4j
public class MonitorHelper {

    private final static String KEY = "key";
    private final static String KEY2 = "key2";
    private final static String STATUS = "status";

    public static void fastCompassOneKey(String metricName, String key, int status, long useTime, boolean isSuccess) {

        FastCompass fastCompass = MetricManager.getFastCompass("",
                new MetricName(
                        metricName, MetricLevel.TRIVIAL).
                        tagged(KEY, key).
                        tagged(STATUS, String.valueOf(status)
                        )
        );

        fastCompass.record(useTime, getSuccess(isSuccess));
    }

    public static void fastCompassTwoKey(String metricName, String key, String key2, int status, long useTime,
                                         boolean isSuccess) {

        FastCompass fastCompass = MetricManager.getFastCompass("",
                new MetricName(
                        metricName, MetricLevel.TRIVIAL).
                        tagged(KEY, key).
                        tagged(KEY2, key2).
                        tagged(STATUS, String.valueOf(status)
                        )
        );

        fastCompass.record(useTime, getSuccess(isSuccess));
    }

    private static String getSuccess(boolean isSuccess) {

        return isSuccess ? "success" : "error";
    }

}
