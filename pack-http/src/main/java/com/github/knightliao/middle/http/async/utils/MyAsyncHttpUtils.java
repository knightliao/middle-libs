package com.github.knightliao.middle.http.async.utils;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.github.knightliao.middle.http.async.utils.helper.MyAsyncHttpClient;
import com.github.knightliao.middle.http.common.constants.HttpConstants;
import com.github.knightliao.middle.http.common.utils.HttpParamUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 12:17
 */
@Slf4j
public class MyAsyncHttpUtils {

    private static MyAsyncHttpClient[] asyncHttpClients;
    private static int asyncHttpCount = HttpParamUtils.asyncHttpClientCount;
    private static int currentWorker = 0;

    static {

        asyncHttpClients = new MyAsyncHttpClient[asyncHttpCount];
        for (int i = 0; i < asyncHttpCount; ++i) {
            MyAsyncHttpClient newHttp = new MyAsyncHttpClient(HttpParamUtils.asyncIoPerClient,
                    HttpParamUtils.asyncMaxConnTotal, HttpParamUtils.asyncConnPerRoute, "async-httpclient" + i);
            asyncHttpClients[i] = newHttp;
        }
    }

    public static CompletionStage<String> get(String url, int timeoutMs) {

        return getMyAsyncHttpClient().get(url, timeoutMs).thenApply(httpResponse -> getContent(url, httpResponse));
    }

    public static CompletableFuture<String> get(String url, Map<String, Object> params) {

        return getMyAsyncHttpClient().get(url, params).thenApply(httpResponse -> getContent(url, httpResponse));
    }

    public static CompletableFuture<String> get(String url, Map<String, Object> params, int timeoutMs) {

        return getMyAsyncHttpClient().get(url, params, timeoutMs)
                .thenApply(httpResponse -> getContent(url, httpResponse));
    }

    public static CompletionStage<String> post(String url, Map<String, Object> params, String body, int timeoutMs) {

        return getMyAsyncHttpClient().post(url, params, body, timeoutMs).thenApply(httpResponse -> getContent(url,
                httpResponse));
    }

    private static MyAsyncHttpClient getMyAsyncHttpClient() {
        return asyncHttpClients[Math.abs(currentWorker++ % asyncHttpCount)];
    }

    private static String getContent(String url, HttpResponse httpResponse) {

        try {
            String encoding = processEncode(httpResponse.getEntity());
            String content = IOUtils.toString(httpResponse.getEntity().getContent(), encoding);

            log.debug("url={} content_size={} encoding={}", url, content.length(), encoding);
            return content;
        } catch (Exception e) {
            log.error(e.toString(), e);
            return "{}";
        }
    }

    private static String processEncode(HttpEntity entity) {

        try {
            return entity.getContentType().getValue().split(";")[1].split("=")[1];
        } catch (Exception e) {
            return HttpConstants.CHARSET;
        }
    }
}
