package com.github.knightliao.middle.http.service.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.knightliao.middle.http.service.loadbalance.ILoadBalancer;
import com.github.knightliao.middle.http.service.loadbalance.impl.RandomLoadBalancerImpl;
import com.github.knightliao.middle.http.service.server.IMyHttpServer;
import com.github.knightliao.middle.http.service.server.helper.server.ServerInstance;
import com.github.knightliao.middle.http.service.server.helper.server.ServerStatus;
import com.github.knightliao.middle.http.service.server.helper.server.ServerStatusFactory;
import com.github.knightliao.middle.http.service.server.helper.serverlist.IServerInstanceList;
import com.github.knightliao.middle.http.support.constants.HttpConstants;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 10:59
 */
@Slf4j
public class MyHttpServerImpl implements IMyHttpServer {

    private String serviceName;
    private IServerInstanceList serverInstanceList;
    private ILoadBalancer loadBalancer;
    private final static ILoadBalancer DEFAULT_LOAD_BALANCE = new RandomLoadBalancerImpl();

    private long refreshIntervalTime = 60000;
    private Map<String, ServerStatus> okServers = new ConcurrentHashMap<>();
    private List<ServerStatus> originalServers = new ArrayList<>();

    // update
    private final AtomicBoolean updatingServerFlag = new AtomicBoolean(false);
    private long nextUpdateTime = -1;

    public MyHttpServerImpl(String serviceName,
                            IServerInstanceList serverInstanceList,
                            ILoadBalancer loadBalancer,
                            long refreshIntervalTime) {
        this.serviceName = serviceName;
        this.serverInstanceList = serverInstanceList;
        this.loadBalancer = loadBalancer;
        if (refreshIntervalTime > 0) {
            this.refreshIntervalTime = refreshIntervalTime;
        }
    }

    private void initServerList() {
        try {
            for (ServerInstance serverInstance : serverInstanceList.serverInstanceList()) {

                ServerStatus serverStatus = ServerStatusFactory.getServerStatus(serverInstance);
                okServers.put(serverInstance.getId(), serverStatus);
                originalServers.add(serverStatus);
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
    }

    @Override
    public ServerStatus getServer() {

        //
        updateServerList();

        //
        Collection<ServerStatus> serverStatuses = okServers.values();
        List<ServerStatus> avaiableServerList = new ArrayList<>(serverStatuses);

        for (ServerStatus serverStatus : serverStatuses) {
            if (serverStatus.getServerInstance().checkAvaiable()) {
                avaiableServerList.add(serverStatus);
            }
        }

        //
        if (avaiableServerList.isEmpty()) {
            return DEFAULT_LOAD_BALANCE.select(originalServers);
        }

        return loadBalancer.select(avaiableServerList);
    }

    @Override
    public List<ServerStatus> getAllAvailable() {

        // get avaiable
        Collection<ServerStatus> serverStatuses = okServers.values();
        List<ServerStatus> availabServers = new ArrayList<>(serverStatuses.size());
        for (ServerStatus serverStatus : serverStatuses) {
            if (serverStatus.getServerInstance().checkAvaiable()) {
                availabServers.add(serverStatus);
            }
        }

        //
        if (availabServers.isEmpty()) {
            return originalServers;
        }

        return availabServers;
    }

    @Override
    public String getName() {
        return serviceName;
    }

    private void updateServerList() {
        // only allow one thread to update the server list
        if (!updatingServerFlag.compareAndSet(false, true)) {
            return;
        }

        try {

            long now = System.currentTimeMillis();
            if (refreshIntervalTime == 0 || nextUpdateTime > now) {
                return;
            } else {
                nextUpdateTime = now + refreshIntervalTime;
            }

            // add old server to list
            for (ServerInstance serverInstance : serverInstanceList.serverInstanceList()) {
                ServerStatus serverStatus = okServers.get(serverInstance.getId());
                if (serverStatus == null) {
                    serverStatus = ServerStatusFactory.getServerStatus(serverInstance);

                    boolean isOk = serverInstance.ping();
                    if (isOk) {
                        serverInstance.setAvailable(true);
                        okServers.put(serverInstance.getId(), serverStatus);
                    }
                }
            }

            // remove not ok server
            Iterator<Map.Entry<String, ServerStatus>> iterator = okServers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ServerStatus> entry = iterator.next();
                ServerInstance serverInstance = entry.getValue().getServerInstance();
                if (!serverInstance.checkAvaiable()) {
                    HttpConstants.logger.info("remove_server {}", serverInstance.getId());
                    iterator.remove();
                }
            }
        } catch (Exception ex) {

            log.error("update server fail " + ex.toString(), ex);

        } finally {

            updatingServerFlag.set(false);
        }
    }

    @Data
    public static class Builder {
        private String serviceName;
        private IServerInstanceList serverList;
        private ILoadBalancer loadBalancer;

        private long refreshIntervalTime;

        public IMyHttpServer build() {
            return new MyHttpServerImpl(serviceName, serverList, loadBalancer, refreshIntervalTime);
        }
    }
}
