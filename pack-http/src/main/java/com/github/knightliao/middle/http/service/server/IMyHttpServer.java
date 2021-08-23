package com.github.knightliao.middle.http.service.server;

import java.util.List;

import com.github.knightliao.middle.http.service.server.helper.server.ServerStatus;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 10:32
 */
public interface IMyHttpServer {

    ServerStatus getServer();

    List<ServerStatus> getAllAvailable();

    String getName();
}
