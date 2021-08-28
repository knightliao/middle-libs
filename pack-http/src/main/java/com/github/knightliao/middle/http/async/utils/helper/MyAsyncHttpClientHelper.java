package com.github.knightliao.middle.http.async.utils.helper;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

import com.github.knightliao.middle.http.common.utils.HttpParamUtils;
import com.github.knightliao.middle.utils.thread.NamedThreadFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 12:35
 */
@Slf4j
public class MyAsyncHttpClientHelper {

    private static ScheduledExecutorService scheduledThreadPoolExecutor = Executors.newScheduledThreadPool(
            HttpParamUtils.cpuProcessorCount, new NamedThreadFactory("async-timeout", true));

    private static <T> CompletionStage<T> timeout(long timeout) {

        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        scheduledThreadPoolExecutor.schedule(() -> {
            completableFuture.completeExceptionally(new TimeoutException());
        }, timeout, TimeUnit.MILLISECONDS);

        return completableFuture;
    }

    public static <T> CompletableFuture<T> timeout(CompletionStage<T> completionStage, long timeoutMs, String... msg) {

        if (completionStage.toCompletableFuture().isDone()) {
            return completionStage.toCompletableFuture();
        }

        CompletionStage<T> resultCompleteStage = completionStage.applyToEither(timeout(timeoutMs), t -> t);

        resultCompleteStage.exceptionally(e -> {
            try {
                completionStage.toCompletableFuture().cancel(true);
            } catch (CancellationException ignore) {

            }
            if (msg.length > 0) {
                log.warn("{} timeout {}", msg, e.toString());
            }
            return null;
        });

        CompletableFuture<T> future = resultCompleteStage.toCompletableFuture();

        return future;
    }

    public static HttpRequestInterceptor createRequestInterceptor() {

        return new HttpRequestInterceptor() {

            @Override
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            }
        };
    }

    public static HttpResponseInterceptor createResponseInterceptor() {
        return new HttpResponseInterceptor() {

            @Override
            public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {

            }
        };
    }
}
