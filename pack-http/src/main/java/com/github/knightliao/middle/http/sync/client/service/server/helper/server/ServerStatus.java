package com.github.knightliao.middle.http.sync.client.service.server.helper.server;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Clock;
import com.codahale.metrics.SlidingTimeWindowReservoir;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 10:32
 */
public class ServerStatus {

    private static final int ERROR_THRESHOLD = 10;

    @Getter
    protected ServerInstance serverInstance;

    protected SlidingTimeWindowReservoir errorPerSecond;

    public void init(ServerInstance serverInstance) {
        this.serverInstance = serverInstance;
        this.errorPerSecond = new SlidingTimeWindowReservoir(1, TimeUnit.SECONDS, new Clock() {
            private long start = System.nanoTime();

            @Override
            public long getTick() {
                return System.nanoTime() - start;
            }
        });
    }

    public void incrErrors() {

        errorPerSecond.update(1);

        int errorCount = errorPerSecond.size();
        if (errorCount >= ERROR_THRESHOLD) {
            serverInstance.triggerShortOff();
        }
    }

    public void connectTimeout() {
        serverInstance.triggerShortOff();
    }

    public void checkAfterException() {
        if (!serverInstance.ping()) {
            serverInstance.triggerShortOff();
        }
    }
}
