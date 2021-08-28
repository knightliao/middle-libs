package com.github.knightliao.middle.http.common.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

import com.github.knightliao.middle.utils.thread.NamedThreadFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 12:46
 */
@Slf4j
public class ConnectionMonitorUtils {

    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
            new ScheduledThreadPoolExecutor(1,
                    new NamedThreadFactory(ConnectionMonitorUtils.class.getName(), true));

    public static void watchAsyncPool(String name,
                                      PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager) {

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {

            PoolStats poolStats = poolingNHttpClientConnectionManager.getTotalStats();

            //
            printPoolStatus("watchAsyncPool", name + "-" + "total", poolStats);
            poolingNHttpClientConnectionManager.getRoutes().forEach(route -> {
                PoolStats subStats = poolingNHttpClientConnectionManager.getStats(route);
                printPoolStatus("watchAsyncPool", name + "-" + route.getTargetHost().getHostName(), subStats);
            });
        }, 1, 1, TimeUnit.MINUTES);
    }

    public static void watchPool(String name,
                                 PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {

            PoolStats poolStats = poolingHttpClientConnectionManager.getTotalStats();

            //
            printPoolStatus("watchPool", name + "-" + "total", poolStats);
            poolingHttpClientConnectionManager.getRoutes().forEach(route -> {
                PoolStats subStats = poolingHttpClientConnectionManager.getStats(route);
                printPoolStatus("watchPool", name + "-" + route.getTargetHost().getHostName(), subStats);
            });
        }, 1, 1, TimeUnit.MINUTES);

    }

    private static void printPoolStatus(String type, String name, PoolStats poolStats) {

        log.debug("connection_monitor={} name={}, pending={} available={} leased={} max={}",
                type,
                name, poolStats.getPending(),
                poolStats.getAvailable(),
                poolStats.getLeased(), poolStats.getMax());
    }
}
