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

    //
    public static int cpuProcessorCount;

    // 异步
    public static int asyncHttpClientCount;
    public static int asyncIoPerClient;
    public static int asyncMaxConnTotal;
    public static int asyncConnPerRoute;

    // 同步
    public static int syncMaxConnTotal;
    public static int syncConnPerRoute;

    // timeout
    public static int syncConnectionTimeout;
    public static int syncSocketTimeout;
    public static int syncConnectionRequestTimeout;

    public static int asyncConnectionTimeout;
    public static int asyncSocketTimeout;
    public static int asyncConnectionRequestTimeout;

    static {

        //
        String cpuProcessorNum = System.getProperty(PREFIX_CONFIG + ".cpu.processor.num");
        cpuProcessorCount = StringUtils.isNoneBlank(cpuProcessorNum) ?
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
        String syncConnectionTimeoutStr = System.getProperty(PREFIX_CONFIG + ".sync.connect.timeout");
        String syncSocketTimeoutStr = System.getProperty(PREFIX_CONFIG + ".sync.socket.timeout");
        String syncConnectionRequestTimeoutStr = System.getProperty(PREFIX_CONFIG + ".sync.connect.request.timeout");
        syncConnectionTimeout =
                StringUtils.isNoneBlank(syncConnectionTimeoutStr) ? Integer.parseInt(syncConnectionTimeoutStr) : 200;
        syncSocketTimeout =
                StringUtils.isNoneBlank(syncSocketTimeoutStr) ? Integer.parseInt(syncSocketTimeoutStr) : 500;
        syncConnectionRequestTimeout =
                StringUtils.isNoneBlank(syncConnectionRequestTimeoutStr) ? Integer.parseInt(
                        syncConnectionRequestTimeoutStr) : 100;

        log.info("syncConnectionTimeout={} syncSocketTimeout={} syncConnectionRequestTimeout={}",
                syncConnectionTimeout, syncSocketTimeout, syncConnectionRequestTimeout);

        String asyncConnectionTimeoutStr = System.getProperty(PREFIX_CONFIG + ".async.connect.timeout");
        String asyncSocketTimeoutStr = System.getProperty(PREFIX_CONFIG + ".async.socket.timeout");
        String asyncConnectionRequestTimeoutStr = System.getProperty(PREFIX_CONFIG + ".async.connect.request.timeout");
        asyncConnectionTimeout =
                StringUtils.isNoneBlank(asyncConnectionTimeoutStr) ? Integer.parseInt(asyncConnectionTimeoutStr) : 20;
        asyncSocketTimeout =
                StringUtils.isNoneBlank(asyncSocketTimeoutStr) ? Integer.parseInt(asyncSocketTimeoutStr) : 200;
        asyncConnectionRequestTimeout =
                StringUtils.isNoneBlank(asyncConnectionRequestTimeoutStr) ?
                        Integer.parseInt(asyncConnectionRequestTimeoutStr) : 5;

        log.info("asyncConnectionTimeout={} asyncSocketTimeout={} asyncConnectionRequestTimeout={}",
                asyncConnectionTimeout, asyncSocketTimeout, asyncConnectionRequestTimeout);
    }
}
