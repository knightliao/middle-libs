package com.github.knightliao.middle.http.async.utils.helper;

import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import com.github.knightliao.middle.http.common.utils.ConnectionMonitorUtils;
import com.github.knightliao.middle.http.common.utils.HttpParamUtils;
import com.github.knightliao.middle.metrics.CostMetricMonitor;
import com.github.knightliao.middle.utils.thread.NamedThreadFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/27 00:23
 */
@Slf4j
public abstract class MyAbstractAsyncHttpClient {

    private volatile CloseableHttpAsyncClient closeableHttpAsyncClient;
    private volatile NHttpClientConnectionManager connManager;

    private IOReactorConfig ioReactorConfig;
    private RequestConfig requestConfig;

    //
    protected final int defaultTimeout = 500;

    private static int costPrint = 200;

    private int total;
    private int connPerRoute;

    private String prefix;

    public MyAbstractAsyncHttpClient(int ioThread, int total, int connPerRoute, String prefix) {

        log.info("init ioThread:{} total:{} connPerRoute:{} ", ioThread, total, connPerRoute);

        ioReactorConfig = IOReactorConfig.copy(IOReactorConfig.DEFAULT)
                .setIoThreadCount(ioThread)
                .setConnectTimeout(HttpParamUtils.asyncConnectionTimeout)
                .setSoKeepAlive(true)
                .setSoLinger(0)
                .build();

        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(HttpParamUtils.asyncConnectionRequestTimeout)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();

        this.total = total;
        this.connPerRoute = connPerRoute;
        this.prefix = prefix;

        //
        doHttpStart();
    }

    private synchronized void doHttpStart() {

        if (closeableHttpAsyncClient != null && closeableHttpAsyncClient.isRunning()) {
            return;
        }

        //
        ThreadFactory threadFactory = new NamedThreadFactory(prefix + "-MyAsyncHttpClient-Exec", false);
        NHttpClientConnectionManager connManager =
                new PoolingNHttpClientConnectionManager(createConnectingIOReactor(IOReactorConfig.DEFAULT,
                        threadFactory));

        //
        closeableHttpAsyncClient = HttpAsyncClients.custom()
                .setMaxConnTotal(total)
                // keep alive
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                // 长连接复用
                .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
                .setThreadFactory(threadFactory)
                .setMaxConnPerRoute(connPerRoute)
                .setDefaultIOReactorConfig(ioReactorConfig)
                .setDefaultRequestConfig(requestConfig)
                .setRedirectStrategy(new DefaultRedirectStrategy())
                .addInterceptorLast(MyAsyncHttpClientHelper.createRequestInterceptor())
                .addInterceptorLast(MyAsyncHttpClientHelper.createResponseInterceptor())
                .setConnectionManager(connManager)
                .build();

        //
        addMonitor();

        //
        closeableHttpAsyncClient.start();
    }

    protected CompletableFuture<HttpResponse> request(HttpUriRequest request) {

        // ensure
        ensureRunning();

        //
        CompletableFuture<HttpResponse> resFuture = new CompletableFuture<>();

        //
        Long startTime = System.currentTimeMillis();
        FutureCallback<HttpResponse> futureCallback = doCallback(request, resFuture, startTime);

        //
        Future<HttpResponse> httpResponseFuture = closeableHttpAsyncClient.execute(request, futureCallback);

        //
        resFuture.exceptionally(e -> {

            httpResponseFuture.cancel(true);

            Long endTime = System.currentTimeMillis();
            if (e instanceof CancellationException) {
                log.warn("requestUrl={} cost={} failed", request.getURI(), endTime - startTime);
            } else {
                log.error("requestUrl={} cost={} failed", request.getURI(), endTime - startTime, e);
            }

            return null;
        });

        return resFuture;
    }

    private FutureCallback<HttpResponse> doCallback(HttpUriRequest request, CompletableFuture<HttpResponse> resFuture
            , Long startTime) {

        FutureCallback<HttpResponse> futureCallback = new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {

                boolean isSuccess = false;
                try {
                    //
                    Long endTime = System.currentTimeMillis();
                    if ((endTime - startTime) >= costPrint) {
                        log.info("requestUrl={} cost={}", request.getURI(), endTime - startTime);
                    }

                    //
                    log.debug("requestUrl={} cost={}", request.getURI(), endTime - startTime);

                    //
                    resFuture.complete(httpResponse);

                    //
                    isSuccess = true;

                } catch (Exception e) {

                    log.error("complete error! url={}, httpResponse={}", request.getURI(), httpResponse, e);
                    resFuture.completeExceptionally(e);

                } finally {
                    //
                    CostMetricMonitor.commitMonitor(prefix, "callback", System.currentTimeMillis() - startTime,
                            isSuccess);
                }

            }

            @Override
            public void failed(Exception ex) {

                resFuture.completeExceptionally(ex);
            }

            @Override
            public void cancelled() {

                log.warn("{} cancelled", request.getURI());
                request.abort();

                resFuture.completeExceptionally(new RuntimeException(String.format("%s cancelled", request.getURI())));
            }
        };

        return futureCallback;
    }

    public String buildUrl(String url, Map<String, Object> params) {

        if (params == null) {
            return url;
        }

        try {

            URIBuilder urlBuilder = new URIBuilder(url);
            params.forEach((key, value) -> {
                if (value != null) {
                    urlBuilder.addParameter(key, value.toString());
                }
            });

            return urlBuilder.getUserInfo();

        } catch (Exception e) {
            log.error(e.toString(), e);
            return url;
        }
    }

    private void ensureRunning() {
        if (closeableHttpAsyncClient.isRunning()) {
            return;
        }

        doHttpStart();
    }

    private void addMonitor() {

        ConnectionMonitorUtils.watchAsyncPool(prefix, (PoolingNHttpClientConnectionManager) connManager);
    }

    private ConnectingIOReactor createConnectingIOReactor(final IOReactorConfig config,
                                                          final ThreadFactory threadFactory) {
        try {
            return new DefaultConnectingIOReactor(config, threadFactory);
        } catch (final IOReactorException ex) {
            throw new IllegalStateException(ex);
        }
    }
}

