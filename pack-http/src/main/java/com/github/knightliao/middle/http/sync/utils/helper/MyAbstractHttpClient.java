package com.github.knightliao.middle.http.sync.utils.helper;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.github.knightliao.middle.http.common.utils.ConnectionMonitorUtils;
import com.github.knightliao.middle.http.common.utils.HttpParamUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 13:05
 */
@Slf4j
public abstract class MyAbstractHttpClient {

    private volatile CloseableHttpClient closeableHttpClient;
    private String prefix;

    public MyAbstractHttpClient(int total, int connPerRoute, String prefix) {

        log.info("total:{} connPerRoute:{} ", total, connPerRoute);
        this.prefix = prefix;

        HttpClientBuilder builder = HttpClientBuilder.create();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(total);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(connPerRoute);
        builder.setConnectionManager(poolingHttpClientConnectionManager);
        closeableHttpClient = builder.build();

        //
        doMonitor(poolingHttpClientConnectionManager);
    }

    private void doMonitor(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {

        try {

            //
            ConnectionMonitorUtils.watchPool(prefix, poolingHttpClientConnectionManager);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    protected CloseableHttpResponse httpExe(HttpRequestBase requestBase,
                                            int connectionTimeout, int readTimeout) throws IOException {

        try {

            setTimeout(requestBase, connectionTimeout, readTimeout);

            return closeableHttpClient.execute(requestBase);

        } catch (SocketTimeoutException ex) {

            log.error("socket_timeout url {} timeout {}", requestBase.getURI(), ex.toString(),
                    ex);
            throw ex;

        } catch (ConnectTimeoutException ex) {

            log.error("connection_timeout url {} timeout {}", requestBase.getURI(), ex.toString(),
                    ex);
            throw ex;
        }
    }

    private static void setTimeout(HttpRequestBase requestBase, int connectionTimeout, int readTimeout) {
        if (readTimeout <= 0) {
            readTimeout = HttpParamUtils.syncSocketTimeout;
        }

        if (connectionTimeout <= 0) {
            connectionTimeout = HttpParamUtils.syncConnectionTimeout;
        }

        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setSocketTimeout(readTimeout)
                .setRedirectsEnabled(true)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(HttpParamUtils.syncConnectionRequestTimeout);
        requestBase.setConfig(builder.build());
    }
}
