package com.github.knightliao.middle.http.sync.client.impl;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.github.knightliao.middle.http.common.service.server.IMyHttpServer;
import com.github.knightliao.middle.http.sync.client.ISimpleHttpClient;
import com.github.knightliao.middle.http.sync.client.impl.helper.IFunctionWrapper;
import com.github.knightliao.middle.http.sync.client.impl.helper.SimpleHttpClientHelper;
import com.github.knightliao.middle.http.sync.utils.helper.MyHttpRawUtils;

;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 10:29
 */
public class SimpleHttpClientDefaultImpl implements ISimpleHttpClient {

    private IMyHttpServer myHttpServer;
    private int numRetries;

    public SimpleHttpClientDefaultImpl(IMyHttpServer myHttpServer, int numRetries) {
        this.myHttpServer = myHttpServer;
        this.numRetries = numRetries;
    }

    @Override
    public String post(String url, List<NameValuePair> params, int connectionTimeout, int readTimeout) {

        IFunctionWrapper functionWrapper = (path -> MyHttpRawUtils.post(path, params, connectionTimeout, readTimeout));

        return SimpleHttpClientHelper.executeWithLoadBalancer(myHttpServer, url, functionWrapper, numRetries);
    }

    @Override
    public String post(String url, List<NameValuePair> params) {

        return post(url, params, 0, 0);
    }

    @Override
    public String post(String url, Map<String, Object> params, int connectionTimeout, int readTimeout) {

        IFunctionWrapper functionWrapper = (path -> MyHttpRawUtils.post(path, params, connectionTimeout, readTimeout));

        return SimpleHttpClientHelper.executeWithLoadBalancer(myHttpServer, url, functionWrapper, numRetries);
    }

    @Override
    public String post(String url, Map<String, Object> params, Map<String, String> headers, int connectionTimeout,
                       int readTimeout) {
        IFunctionWrapper functionWrapper = (path -> MyHttpRawUtils.post(path, params, headers, connectionTimeout,
                readTimeout));

        return SimpleHttpClientHelper.executeWithLoadBalancer(myHttpServer, url, functionWrapper, numRetries);
    }

    @Override
    public String post(String url, String content, int connectionTimeout, int readTimeoutMs) {
        IFunctionWrapper functionWrapper = (path -> MyHttpRawUtils.post(path, content, connectionTimeout, readTimeoutMs));

        return SimpleHttpClientHelper.executeWithLoadBalancer(myHttpServer, url, functionWrapper, numRetries);
    }

    @Override
    public String get(String url, List<NameValuePair> params, int connectionTimeout, int readTimeout) {
        IFunctionWrapper functionWrapper = (path -> MyHttpRawUtils.get(path, params, connectionTimeout, readTimeout));

        return SimpleHttpClientHelper.executeWithLoadBalancer(myHttpServer, url, functionWrapper, numRetries);
    }

    @Override
    public String get(String url, int connectionTimeout, int readTimeout) {
        IFunctionWrapper functionWrapper = (path -> MyHttpRawUtils.get(path, null, connectionTimeout, readTimeout));

        return SimpleHttpClientHelper.executeWithLoadBalancer(myHttpServer, url, functionWrapper, numRetries);
    }
}
