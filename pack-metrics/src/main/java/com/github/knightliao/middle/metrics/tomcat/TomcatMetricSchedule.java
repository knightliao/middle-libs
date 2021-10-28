package com.github.knightliao.middle.metrics.tomcat;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.github.knightliao.middle.metrics.jvm.JvmMetricService;
import com.github.knightliao.middle.utils.net.IpUtils;
import com.github.knightliao.middle.utils.thread.NamedThreadFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/29 00:08
 */
@Slf4j
public class TomcatMetricSchedule {

    private String ip;

    private TomcatMetricService tomcatMetricService;

    public TomcatMetricSchedule(TomcatMetricService tomcatMetricService) {
        this.tomcatMetricService = tomcatMetricService;

        init();
    }

    private void init() {

        this.ip = IpUtils.getLocalIp();
        printLog();

        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1,
                new NamedThreadFactory(TomcatMetricSchedule.class.getName(), true));
        pool.scheduleAtFixedRate(this::printLog, 1, 1, TimeUnit.MINUTES);
    }

    private void printLog() {

        tomcatLog();

        jvmLog();
    }

    private void tomcatLog() {
        log.info(" " + ip + " " + tomcatMetricService.getMetrics().toString()
                .replace("{", "").replace("}", ""));
    }

    private void jvmLog() {
        log.info(" " + ip + " " + JvmMetricService.getMetrics().toString()
                .replace("{", "").replace("}", ""));
    }
}
