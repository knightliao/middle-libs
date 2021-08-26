package com.github.knightliao.middle.http.async.client.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.github.knightliao.middle.http.async.client.ISimpleAsyncHttpClient;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/27 00:16
 */
public class SimpleAsyncHttpClientDefaultImpl implements ISimpleAsyncHttpClient {

    @Override
    public CompletionStage<String> get(String url, int timeout) {
        return null;
    }

    @Override
    public CompletableFuture<String> get(String url, Map<String, Object> params) {
        return null;
    }

    @Override
    public CompletableFuture<String> get(String url, Map<String, Object> params, int timeout) {
        return null;
    }

    @Override
    public CompletableFuture<String> get(String url, Map<String, Object> params, Map<String, String> headers,
                                         int timeout) {
        return null;
    }

    @Override
    public CompletableFuture<String> post(String url, Map<String, Object> params) {
        return null;
    }

    @Override
    public CompletableFuture<String> post(String url, Map<String, Object> params, int timeout) {
        return null;
    }

    @Override
    public CompletableFuture<String> post(String url, Map<String, Object> params, String body,
                                          Map<String, String> headers, int timeout) {
        return null;
    }

}
