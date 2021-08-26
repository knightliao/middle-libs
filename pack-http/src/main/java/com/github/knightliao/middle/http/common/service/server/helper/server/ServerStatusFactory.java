package com.github.knightliao.middle.http.common.service.server.helper.server;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 11:43
 */
public class ServerStatusFactory {

    public static ServerStatus getServerStatus(ServerInstance serverInstance) {
        ServerStatus serverStatus = new ServerStatus();
        serverStatus.init(serverInstance);
        return serverStatus;
    }
}
