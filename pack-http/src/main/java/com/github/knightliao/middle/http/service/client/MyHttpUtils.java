package com.github.knightliao.middle.http.service.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 01:10
 */
@Slf4j
public class MyHttpUtils {

    private static final int MAX_PRE_ROUTE = 20;
    private static final int MAX_TOTAL_ROUTE = 100;

    static {
        HttpClientBuilder builder = HttpClientBuilder.create();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL_ROUTE);
        connectionManager.setDefaultMaxPerRoute(MAX_PRE_ROUTE);
        builder.setConnectionManager(connectionManager);

        MyHttpUtilsHelper.closeableHttpClient = builder.build();

        String value = System.getProperty("http.request.time.out");
        if (!StringUtils.isEmpty(value)) {
            int timeout = 0;
            try {
                timeout = Integer.valueOf(value);
            } catch (Exception ex) {

            }

            if (timeout > 0) {
                MyHttpUtilsHelper.REQUEST_TIMEOUT = timeout;
            }
        }
    }

    //
    public static CloseableHttpResponse get(String url, List<NameValuePair> params, int connectTimeout,
                                            int readTimeout) throws IOException {

        return MyHttpUtilsHelper.httpExe(
                MyHttpUtilsHelper.getHttpGet(url, params), connectTimeout, readTimeout);
    }

    //
    public static CloseableHttpResponse get(String url, int connectTimeout,
                                            int readTimeout) throws IOException {

        return get(url, null, connectTimeout, readTimeout);
    }

    //
    public static CloseableHttpResponse post(String url, List<NameValuePair> params, int connectTimeout,
                                             int readTimeout) throws IOException {

        return MyHttpUtilsHelper.httpExe(
                MyHttpUtilsHelper.getHttpPost(url, params, null), connectTimeout, readTimeout);
    }

    public static CloseableHttpResponse post(String url, Map<String, Object> params, int connectTimeout,
                                             int readTimeout) throws IOException {

        return post(url, params, null, connectTimeout, readTimeout);
    }

    public static CloseableHttpResponse post(String url, Map<String, Object> params,
                                             Map<String, String> headers, int connectTimeout,
                                             int readTimeout) throws IOException {

        List<NameValuePair> pairs = new ArrayList<>(params.size());
        for (String key : params.keySet()) {
            pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
        }

        return MyHttpUtilsHelper.httpExe(
                MyHttpUtilsHelper.getHttpPost(url, pairs, headers), connectTimeout, readTimeout);
    }

    public static CloseableHttpResponse post(String url, String content, int connectTimeout,
                                             int readTimeout) throws IOException {

        return MyHttpUtilsHelper.httpExe(
                MyHttpUtilsHelper.getHttpPost(url, content), connectTimeout, readTimeout);
    }

}
