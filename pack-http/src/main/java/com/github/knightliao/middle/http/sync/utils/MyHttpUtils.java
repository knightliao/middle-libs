package com.github.knightliao.middle.http.sync.utils;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 21:15
 */
@Slf4j
public class MyHttpUtils {

    //
    public static String get(String url, List<NameValuePair> params, int connectTimeout,
                             int readTimeout) throws IOException {

        return getResult(url, MyHttpRawUtils.get(url, params, connectTimeout, readTimeout));
    }

    //
    public static String get(String url, int connectTimeout,
                             int readTimeout) throws IOException {

        return getResult(url, MyHttpRawUtils.get(url, connectTimeout, readTimeout));
    }

    //
    public static String post(String url, List<NameValuePair> params, int connectTimeout,
                              int readTimeout) throws IOException {
        return getResult(url, MyHttpRawUtils.post(url, params, connectTimeout, readTimeout));
    }

    public static String post(String url, Map<String, Object> params, int connectTimeout,
                              int readTimeout) throws IOException {
        return getResult(url, MyHttpRawUtils.post(url, params, connectTimeout, readTimeout));
    }

    public static String post(String url, Map<String, Object> params,
                              Map<String, String> headers, int connectTimeout,
                              int readTimeout) throws IOException {
        return getResult(url, MyHttpRawUtils.post(url, params, headers, connectTimeout, readTimeout));
    }

    public static String post(String url, String content, int connectTimeout,
                              int readTimeout) throws IOException {
        return getResult(url, MyHttpRawUtils.post(url, content, connectTimeout, readTimeout));
    }

    private static String getResult(String url, CloseableHttpResponse closeableHttpResponse) throws IOException {

        try {

            //
            StatusLine statusLine = closeableHttpResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                log.warn("http status {}", statusLine.getStatusCode());
                throw new ServerException("server internal error " + url);
            }

            //
            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                return data;
            } else {
                log.warn("http entity null {}", url);
            }

        } finally {

            IOUtils.closeQuietly(closeableHttpResponse);
        }

        return null;
    }
}
