package com.github.knightliao.middle.http.service.loadbalance.impl;

import java.util.List;
import java.util.Random;

import com.github.knightliao.middle.http.service.loadbalance.ILoadBalancer;
import com.github.knightliao.middle.http.service.server.helper.server.ServerStatus;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 11:53
 */
public class RandomLoadBalancerImpl implements ILoadBalancer {

    private Random random = new Random(this.hashCode());

    @Override
    public ServerStatus select(List<ServerStatus> serverStatusList) {

        int index = random.nextInt(serverStatusList.size());
        return serverStatusList.get(index);
    }
}
