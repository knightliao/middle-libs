package com.github.knightliao.middle.http.async.utils.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.github.knightliao.middle.http.common.constants.HttpConstants;
import com.github.knightliao.middle.http.common.utils.HttpParamUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 14:13
 */
@Slf4j
public class MyAsyncHttpClient extends MyAbstractAsyncHttpClient {

    public MyAsyncHttpClient(int ioThread, int total, int connPerRoute, String prefix) {
        super(ioThread, total, connPerRoute, prefix);
    }

    public CompletableFuture<HttpResponse> get(String url) {
        return get(url, defaultTimeout);
    }

    public CompletableFuture<HttpResponse> get(String url, int timeoutMs) {

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getRequestConfig(timeoutMs));
        return MyAsyncHttpClientHelper.timeout(request(httpGet), timeoutMs, url);
    }

    public CompletableFuture<HttpResponse> get(String url, Map<String, Object> params) {

        return get(buildUrl(url, params));
    }

    public CompletableFuture<HttpResponse> get(String url, Map<String, Object> params, int timeoutMs) {

        return get(buildUrl(url, params), timeoutMs);
    }

    public CompletableFuture<HttpResponse> get(String url, Map<String, Object> params,
                                               Map<String, String> headers, int timeoutMs) {

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getRequestConfig(timeoutMs));

        if (headers != null) {
            headers.forEach(httpGet::setHeader);
        }

        return MyAsyncHttpClientHelper.timeout(request(httpGet), timeoutMs, url);
    }

    public CompletableFuture<HttpResponse> post(String url, Map<String, Object> params,
                                                Map<String, String> bodyParamsMap, int timeoutMs) throws
            UnsupportedEncodingException {

        //
        HttpPost httpPost = new HttpPost(buildUrl(url, params));
        httpPost.setConfig(getRequestConfig(timeoutMs));

        //
        List<NameValuePair> bodyNamesList = new ArrayList<>();
        bodyParamsMap.forEach((key, value) -> bodyNamesList.add(new BasicNameValuePair(key, value)));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(bodyNamesList, HttpConstants.CHARSET);

        //
        httpPost.setEntity(urlEncodedFormEntity);

        return request(httpPost);
    }

    public CompletableFuture<HttpResponse> post(String url, String body) {
        //
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(body, HttpConstants.CHARSET));
        httpPost.setConfig(getRequestConfig(defaultTimeout));

        return request(httpPost);
    }

    public CompletableFuture<HttpResponse> post(String url, String body, int timeoutMs) {
        //
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(body, HttpConstants.CHARSET));
        httpPost.setConfig(getRequestConfig(timeoutMs));

        return request(httpPost);
    }

    public CompletableFuture<HttpResponse> post(String url, Map<String, Object> params, String body) {
        return post(buildUrl(url, params), body);
    }

    public CompletableFuture<HttpResponse> post(String url, Map<String, Object> params, String body, int timeoutMs) {
        return post(buildUrl(url, params), body, timeoutMs);
    }

    public CompletableFuture<HttpResponse> post(String url, Map<String, Object> params, String body,
                                                Map<String, String> headers, int timeoutMs) {
        return post(buildUrl(url, params), body, headers, timeoutMs);
    }

    public CompletableFuture<HttpResponse> post(String url, String body,
                                                Map<String, String> headers, int timeoutMs) {

        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(getRequestConfig(timeoutMs));
        if (body != null) {
            httpPost.setEntity(new StringEntity(body, HttpConstants.CHARSET));
        }
        if (headers != null) {
            headers.forEach(httpPost::setHeader);
        }

        return MyAsyncHttpClientHelper.timeout(request(httpPost), timeoutMs, url);
    }

    public CompletableFuture<HttpResponse> post(String url, byte[] body,
                                                Map<String, String> headers, int timeoutMs) {

        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(getRequestConfig(timeoutMs));
        if (body != null) {
            httpPost.setEntity(new ByteArrayEntity(body));
        }
        if (headers != null) {
            headers.forEach(httpPost::setHeader);
        }

        return MyAsyncHttpClientHelper.timeout(request(httpPost), timeoutMs, url);
    }

    private RequestConfig getRequestConfig(int timeoutMs) {

        return RequestConfig.custom()
                .setSocketTimeout(timeoutMs)
                .setConnectionRequestTimeout(HttpParamUtils.asyncConnectionRequestTimeout)
                .setConnectTimeout(HttpParamUtils.asyncConnectionTimeout)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();
    }
}

