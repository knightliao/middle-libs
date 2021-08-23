package com.github.knightliao.middle.http.sync.client.service.loadbalance;

import java.util.List;

import com.github.knightliao.middle.http.sync.client.service.server.helper.server.ServerStatus;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 11:49
 */
public interface ILoadBalancer {

    ServerStatus select(List<ServerStatus> serverStatusList);
}
