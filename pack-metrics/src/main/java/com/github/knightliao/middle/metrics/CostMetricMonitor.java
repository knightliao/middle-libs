package com.github.knightliao.middle.metrics;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 13:51
 */
@Slf4j
public class CostMetricMonitor {

    public static void commitMonitor(String prefix, String node, long cost, boolean isSucc) {

        try {

            String costStr;
            if (cost == 0) {
                costStr = "0";
            } else if (cost <= 1) {
                costStr = "0_1";
            } else if (cost <= 5) {
                costStr = "1_5";
            } else if (cost <= 10) {
                costStr = "5_10";
            } else {
                costStr = "10_";
            }

            MonitorHelper.doMeter(prefix, isSucc, node, costStr);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }
}
