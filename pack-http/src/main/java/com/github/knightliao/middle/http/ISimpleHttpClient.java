package com.github.knightliao.middle.http;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 10:25
 */
public interface ISimpleHttpClient {

    String post(String url, List<NameValuePair> params, int connectionTimeout, int readTimeout);

    String post(String url, List<NameValuePair> params);

    String post(String url, Map<String, Object> params, int connectionTimeout, int readTimeout);

    String post(String url, Map<String, Object> params, Map<String, String> headers,
                int connectionTimeout, int readTimeout);

    String post(String url, String content,
                int connectionTimeout, int readTimeout);

    String get(String url, List<NameValuePair> params, int connectionTimeout, int readTimeout);

    String get(String url, int connectionTimeout, int readTimeout);
}
