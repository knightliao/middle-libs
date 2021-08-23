package com.github.knightliao.middle.http;

import com.github.knightliao.middle.http.impl.SimpleHttpClientDefaultImpl;
import com.github.knightliao.middle.http.service.loadbalance.ILoadBalancer;
import com.github.knightliao.middle.http.service.loadbalance.impl.RoundRobinLoadBalancerImpl;
import com.github.knightliao.middle.http.service.server.IMyHttpServer;
import com.github.knightliao.middle.http.service.server.helper.serverlist.DefaultServerInstanceList;
import com.github.knightliao.middle.http.service.server.helper.serverlist.IServerInstanceList;
import com.github.knightliao.middle.http.service.server.impl.MyHttpServerImpl;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 14:25
 */
public abstract class SimpleHttpSupport {

    protected ISimpleHttpClient simpleHttpClient;
    protected int connectionTimeout = 200;
    protected int readTimeout = 200;

    protected void init(String serverName, String host, boolean isHttps, int numRetries) {

        //
        IServerInstanceList serverInstanceList = new DefaultServerInstanceList(serverName, host, isHttps, 5000);
        ILoadBalancer loadBalancer = new RoundRobinLoadBalancerImpl();

        //
        MyHttpServerImpl.Builder builder = new MyHttpServerImpl.Builder();
        builder.setLoadBalancer(loadBalancer);
        builder.setRefreshIntervalTime(10 * 1000);
        builder.setServerList(serverInstanceList);
        builder.setServiceName(serverName);
        IMyHttpServer myHttpServer = builder.build();

        //
        simpleHttpClient = new SimpleHttpClientDefaultImpl(myHttpServer, numRetries);
    }
}
