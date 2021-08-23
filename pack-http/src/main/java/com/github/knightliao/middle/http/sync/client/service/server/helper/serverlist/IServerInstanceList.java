package com.github.knightliao.middle.http.sync.client.service.server.helper.serverlist;

import java.util.List;

import com.github.knightliao.middle.http.sync.client.service.server.helper.server.ServerInstance;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 11:01
 */
public interface IServerInstanceList {

    String serverName();

    List<ServerInstance> serverInstanceList();
}
