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

    private static final String PREFIX_CONFIG = "httpclient";

    public static int asyncHttpClientCount;
    public static int asyncIoPerClient;
    public static int asyncMaxConnTotal;
    public static int asyncConnPerRoute;

    public static int syncMaxConnTotal;
    public static int syncConnPerRoute;

    public static int connectionTimeout;
    public static int socketTimeout;
    public static int requestTimeout;

    static {

        //
        String cpuProcessorNum = System.getProperty(PREFIX_CONFIG + ".cpu.processor.num");
        int cpuProcessorCount = StringUtils.isNoneBlank(cpuProcessorNum) ?
                Integer.parseInt(cpuProcessorNum) : Runtime.getRuntime().availableProcessors();

        //
        String asyncIoPerClientStr = System.getProperty(PREFIX_CONFIG + ".asyncIoPerClient");
        String asyncHttpClientCountStr = System.getProperty(PREFIX_CONFIG + ".asyncHttpClientCount");

        asyncIoPerClient =
                StringUtils.isNoneBlank(asyncIoPerClientStr) ? Integer.parseInt(asyncIoPerClientStr)
                        : cpuProcessorCount;
        asyncHttpClientCount =
                StringUtils.isNoneBlank(asyncHttpClientCountStr) ? Integer.parseInt(asyncHttpClientCountStr) : 2;
        log.info("asyncHttpClientCount={} cpuProcessorCount={} asyncIoPerClient={}",
                asyncHttpClientCount, cpuProcessorCount, asyncIoPerClient);

        //
        String maxConnTotalStr = System.getProperty(PREFIX_CONFIG + ".asyncMaxConnTotal");
        String connPerRouteStr = System.getProperty(PREFIX_CONFIG + ".asyncConnPerRoute");
        String syncMaxConnTotalStr = System.getProperty(PREFIX_CONFIG + ".syncMaxConnTotal");
        String syncConnPerRouteStr = System.getProperty(PREFIX_CONFIG + ".syncConnPerRoute");

        asyncMaxConnTotal = StringUtils.isNoneBlank(maxConnTotalStr) ? Integer.parseInt(maxConnTotalStr) : 5000;
        asyncConnPerRoute = StringUtils.isNoneBlank(connPerRouteStr) ? Integer.parseInt(connPerRouteStr) : 500;
        syncMaxConnTotal = StringUtils.isNoneBlank(syncMaxConnTotalStr) ? Integer.parseInt(syncMaxConnTotalStr) : 100;
        syncConnPerRoute = StringUtils.isNoneBlank(syncConnPerRouteStr) ? Integer.parseInt(syncConnPerRouteStr) : 20;
        log.info("asyncMaxConnTotal={} asyncConnPerRoute={} syncMaxConnTotal={} syncConnPerRoute={}",
                asyncMaxConnTotal, asyncConnPerRoute, syncMaxConnTotal, syncConnPerRoute);

        //
        String connectionTimeoutStr = System.getProperty(PREFIX_CONFIG + ".connect.timeout");
        String socketTimeoutStr = System.getProperty(PREFIX_CONFIG + ".socket.timeout");
        String requestTimeoutStr = System.getProperty(PREFIX_CONFIG + ".request.timeout");
        connectionTimeout =
                StringUtils.isNoneBlank(connectionTimeoutStr) ? Integer.parseInt(connectionTimeoutStr) : 200;
        socketTimeout = StringUtils.isNoneBlank(socketTimeoutStr) ? Integer.parseInt(socketTimeoutStr) : 500;
        requestTimeout = StringUtils.isNoneBlank(requestTimeoutStr) ? Integer.parseInt(requestTimeoutStr) : 100;
        log.info("connectionTimeout={} socketTimeout={} requestTimeout={}",
                connectionTimeout, socketTimeout, requestTimeout);
    }
}
