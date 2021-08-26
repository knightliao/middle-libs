package com.github.knightliao.middle.http.common.service.loadbalance.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.knightliao.middle.http.common.service.loadbalance.ILoadBalancer;
import com.github.knightliao.middle.http.common.service.server.helper.server.ServerStatus;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 11:50
 */
public class RoundRobinLoadBalancerImpl implements ILoadBalancer {

    private static AtomicInteger counter = new AtomicInteger();

    @Override
    public ServerStatus select(List<ServerStatus> serverStatusList) {

        int count = counter.incrementAndGet();
        if (count < 0) {
            count = 0;
            counter.set(0);
        }

        int index = count % serverStatusList.size();
        return serverStatusList.get(index);
    }
}
