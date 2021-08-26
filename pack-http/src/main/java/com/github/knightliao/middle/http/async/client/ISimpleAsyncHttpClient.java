package com.github.knightliao.middle.http.async.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/27 00:06
 */
public interface ISimpleAsyncHttpClient {

    CompletionStage<String> get(String url, int timeout);

    CompletableFuture<String> get(String url, Map<String, Object> params);

    CompletableFuture<String> get(String url, Map<String, Object> params, int timeout);

    CompletableFuture<String> get(String url, Map<String, Object> params, Map<String, String> headers, int timeout);

    CompletableFuture<String> post(String url, Map<String, Object> params);

    CompletableFuture<String> post(String url, Map<String, Object> params, int timeout);

    CompletableFuture<String> post(String url, Map<String, Object> params, String body, Map<String, String> headers,
                                   int timeout);

}
