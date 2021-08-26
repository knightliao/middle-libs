package com.github.knightliao.middle.http.common.service.server.helper.serverlist;

import static java.util.Arrays.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.knightliao.middle.http.common.service.server.helper.server.ServerInstance;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 11:03
 */
public class DefaultServerInstanceList implements IServerInstanceList {

    private String serverName;
    private List<ServerInstance> list = new ArrayList<>();

    public DefaultServerInstanceList(String serverName, String serverList, boolean isSec, long offDuration) {

        this.serverName = serverName;

        if (StringUtils.isEmpty(serverList)) {
            throw new RuntimeException("serverList is empty");
        }

        //
        list = stream(serverList.split(","))
                .map(host -> host.split(":"))
                .filter(strings -> strings.length == 2)
                .map(strings ->
                        new ServerInstance(serverName,
                                strings[0] + ":" + Integer.valueOf(strings[1]),
                                isSec, offDuration))
                .peek(item -> item.setAvailable(true))
                .collect(Collectors.toList());
    }

    @Override
    public String serverName() {
        return serverName;
    }

    @Override
    public List<ServerInstance> serverInstanceList() {
        return list;
    }
}
