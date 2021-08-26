package com.github.knightliao.middle.http.common.utils;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/27 00:45
 */
@Slf4j
public class HttpParamUtils {

    public static int httpClientCount;
    public static int ioPerClient;
    public static int maxConnTotal;
    public static int connPerRoute;

    public static int connectionTimeout;
    public static int socketTimeout;
    public static int requestTimeout;

    static {

        //
        String cpuProcessorNum = System.getProperty("cpu.processor.num");
        int cpuProcessorCount = StringUtils.isNoneBlank(cpuProcessorNum) ?
                Integer.valueOf(cpuProcessorNum) : Runtime.getRuntime().availableProcessors();

        //
        String ioPerClientStr = System.getProperty("httpclient.ioNum");
        String httpClientCountStr = System.getProperty("httpclient.count");

        ioPerClient = StringUtils.isNoneBlank(ioPerClientStr) ? Integer.valueOf(ioPerClientStr) : cpuProcessorCount;
        httpClientCount = StringUtils.isNoneBlank(httpClientCountStr) ? Integer.valueOf(httpClientCountStr) : 2;
        log.info("http_client_count={} total_cpu={} iothread_per_client={}",
                httpClientCount, cpuProcessorCount, ioPerClient);

        //
        String maxConnTotalStr = System.getProperty("httpclient.maxConnTotal");
        String connPerRouteStr = System.getProperty("httpclient.perConnRoute");

        maxConnTotal = StringUtils.isNoneBlank(maxConnTotalStr) ? Integer.valueOf(maxConnTotalStr) : 5000;
        connPerRoute = StringUtils.isNoneBlank(connPerRouteStr) ? Integer.valueOf(connPerRouteStr) : 500;
        log.info("http_client_maxConnTotal={} http_client_connPerRoute={}",
                maxConnTotal, connPerRoute);

        //
        String connectionTimeoutStr = System.getProperty("httpclient.connect.timeout");
        String socketTimeoutStr = System.getProperty("httpclient.socket.timeout");
        String requestTimeoutStr = System.getProperty("httpclient.request.timeout");
        connectionTimeout = StringUtils.isNoneBlank(connectionTimeoutStr) ? Integer.valueOf(connectionTimeoutStr) : 200;
        socketTimeout = StringUtils.isNoneBlank(socketTimeoutStr) ? Integer.valueOf(socketTimeoutStr) : 500;
        requestTimeout = StringUtils.isNoneBlank(requestTimeoutStr) ? Integer.valueOf(requestTimeoutStr) : 100;
        log.info("http_client_connect_timeout={} http_client_socket_timeout={} http_client_request_timeout={}",
                connectionTimeout, socketTimeout, requestTimeout);
    }
}
