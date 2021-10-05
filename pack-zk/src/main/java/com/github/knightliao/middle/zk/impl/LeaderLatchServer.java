package com.github.knightliao.middle.zk.impl;

import java.io.Closeable;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/5 21:32
 */
@Slf4j
public class LeaderLatchServer implements LeaderLatchListener, Closeable {
    /**
     * server name
     **/
    @Getter
    private final String serverName;

    @Getter
    private LeaderLatch leaderLatch;

    @Setter
    private Runnable onTakeLead;

    @Setter
    private Runnable onLostLead;

    public LeaderLatchServer(CuratorFramework client, String path, String serverName) {

        this.serverName = serverName;
        /** 传入客户端、监听路径、监听器 */
        leaderLatch = new LeaderLatch(client, path);
        leaderLatch.addListener(this);
    }

    public void start() throws Exception {
        leaderLatch.start();
        log.info(serverName + " started done");
    }

    @Override
    public void close() throws IOException {
        leaderLatch.close();
    }

    @Override
    public void isLeader() {

        synchronized(this) {

            doCallback();

        }
    }

    @Override
    public void notLeader() {
        synchronized(this) {

            doCallback();
        }
    }

    private void doCallback() {
        if (leaderLatch.hasLeadership()) {
            if (onTakeLead != null) {
                onTakeLead.run();
            }
            log.info(serverName + " has gained leadership");
        } else {
            if (onLostLead != null) {
                onLostLead.run();
            }
            log.info(serverName + " has lost leadership");
        }
    }
}
