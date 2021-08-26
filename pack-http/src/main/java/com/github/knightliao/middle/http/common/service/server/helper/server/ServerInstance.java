package com.github.knightliao.middle.http.common.service.server.helper.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.github.knightliao.middle.http.common.constants.HttpConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 10:33
 */
@Getter
@Slf4j
public class ServerInstance {

    private String serviceName;
    private String id;
    private String hostAndPort;
    private String url;

    @Setter
    private boolean available;

    // 不可用持续时长
    private long shortCircuitDuration;
    // 是否不可用
    private volatile boolean shortCircuited = false;
    // 不可用超时时间
    private volatile long shortCircuitExpiration;

    public ServerInstance(String serviceName, String hostPort, boolean isSec, long duration) {
        this.serviceName = serviceName;
        this.id = hostPort;
        this.shortCircuitDuration = duration;
        this.hostAndPort = hostPort;

        if (isSec) {
            this.url = "https://" + hostPort;
        } else {
            this.url = "http://" + hostPort;
        }
    }

    public void triggerShortOff() {
        if (shortCircuited) {
            return;
        }

        final long now = System.currentTimeMillis();
        shortCircuitExpiration = now + shortCircuitDuration;
        shortCircuited = true;

        HttpConstants.logger.info("triggerShortOff {} TRIGGER_OFFLINE {} CAN_ONLINE {} EXPIRATION",
                hostAndPort, shortCircuitDuration, shortCircuitExpiration);
    }

    public boolean checkAvaiable() {
        return available && isShortCircuitOver();
    }

    private boolean isShortCircuitOver() {
        if (shortCircuited) {
            long now = System.currentTimeMillis() - shortCircuitExpiration;
            if (now >= 0 && ping()) {
                shortCircuited = false;
                HttpConstants.logger.info("isShortCircuitOver {} online", hostAndPort);
            }
        }

        return !shortCircuited;
    }

    public boolean ping() {

        Socket socket = new Socket();

        try {

            String[] strs = hostAndPort.split(":");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(strs[0], Integer.parseInt(strs[1]));
            socket.connect(inetSocketAddress, 100);
            return socket.isConnected();

        } catch (Exception ex) {
            return false;
        } finally {

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
