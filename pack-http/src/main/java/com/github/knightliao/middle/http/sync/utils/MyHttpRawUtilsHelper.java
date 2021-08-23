package com.github.knightliao.middle.http.sync.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 09:51
 */
@Slf4j
public class MyHttpRawUtilsHelper {

    protected static final String CHARSET = "UTF-8";
    protected static final int CONNECTION_TIMEOUT = 200;
    protected static final int SOCKET_TIMEOUT = 500;
    protected static int REQUEST_TIMEOUT = 100;

    protected static CloseableHttpClient closeableHttpClient;

    protected static HttpGet getHttpGet(String url, List<NameValuePair> params) {

        StringBuilder sb = new StringBuilder(url);
        if (StringUtils.containsNone(url, "?")) {
            sb.append("?");
        }

        if (params != null) {
            String paramStr = URLEncodedUtils.format(params, CHARSET);
            sb.append(paramStr);
        }

        return new HttpGet(sb.toString());
    }

    protected static HttpPost getHttpPost(String url, List<NameValuePair> params, Map<String, String> headers) {

        try {
            HttpPost post = new HttpPost(url);
            if (headers != null && headers.size() > 0) {
                headers.forEach(post::setHeader);
            }

            post.setEntity(new UrlEncodedFormEntity(params, CHARSET));
            return post;
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    protected static HttpPost getHttpPost(String url, String content) {

        HttpPost post = new HttpPost(url);

        ContentType contentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), CHARSET);
        post.setEntity(new StringEntity(content, contentType));

        return post;
    }

    protected static CloseableHttpResponse httpExe(HttpRequestBase requestBase,
                                                   int connectionTimeout, int readTimeout) throws IOException {

        CloseableHttpResponse closeableHttpResponse = null;

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
            readTimeout = SOCKET_TIMEOUT;
        }

        if (connectionTimeout <= 0) {
            connectionTimeout = CONNECTION_TIMEOUT;
        }

        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setSocketTimeout(readTimeout)
                .setRedirectsEnabled(true)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT);
        requestBase.setConfig(builder.build());
    }
}
