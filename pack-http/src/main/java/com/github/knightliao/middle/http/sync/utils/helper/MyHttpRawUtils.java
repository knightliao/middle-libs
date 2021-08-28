package com.github.knightliao.middle.http.sync.utils.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;

import com.github.knightliao.middle.http.common.utils.HttpParamUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 01:10
 */
@Slf4j
public class MyHttpRawUtils {

    // 每个连接最多的并发
    private static final int MAX_PRE_ROUTE = HttpParamUtils.syncConnPerRoute;
    // 全局最多的并发
    private static final int MAX_TOTAL_ROUTE = HttpParamUtils.syncMaxConnTotal;

    private static MyHttpClient httpClient;

    static {

        httpClient = new MyHttpClient(MAX_TOTAL_ROUTE, MAX_PRE_ROUTE, "my_http_client");
    }

    //
    public static CloseableHttpResponse get(String url, List<NameValuePair> params, int connectTimeout,
                                            int readTimeout) throws IOException {

        return httpClient.httpExe(
                MyHttpRawUtilsHelper.getHttpGet(url, params), connectTimeout, readTimeout);
    }

    //
    public static CloseableHttpResponse get(String url, int connectTimeout,
                                            int readTimeout) throws IOException {

        return get(url, null, connectTimeout, readTimeout);
    }

    //
    public static CloseableHttpResponse post(String url, List<NameValuePair> params, int connectTimeout,
                                             int readTimeout) throws IOException {

        return httpClient.httpExe(
                MyHttpRawUtilsHelper.getHttpPost(url, params, null), connectTimeout, readTimeout);
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

        return httpClient.httpExe(
                MyHttpRawUtilsHelper.getHttpPost(url, pairs, headers), connectTimeout, readTimeout);
    }

    public static CloseableHttpResponse post(String url, String content, int connectTimeout,
                                             int readTimeout) throws IOException {

        return httpClient.httpExe(
                MyHttpRawUtilsHelper.getHttpPost(url, content), connectTimeout, readTimeout);
    }

}
