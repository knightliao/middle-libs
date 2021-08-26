package com.github.knightliao.middle.http.async.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.reactor.IOReactorConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/27 00:23
 */
@Slf4j
public abstract class MyAbstractAsyncHttpClient {

    private volatile CloseableHttpAsyncClient closeableHttpAsyncClient;

    protected final int defaultTimeout = 500;

    private static int costPrint = 200;

    private IOReactorConfig ioReactorConfig;
    private RequestConfig requestConfig;

    private int total;
    private int connPerRoute;




}

