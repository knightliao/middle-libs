package com.github.knightliao.middle.http.sync.utils.helper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.github.knightliao.middle.http.common.constants.HttpConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 09:51
 */
@Slf4j
public class MyHttpRawUtilsHelper {

    protected static HttpGet getHttpGet(String url, List<NameValuePair> params) {

        StringBuilder sb = new StringBuilder(url);
        if (StringUtils.containsNone(url, "?")) {
            sb.append("?");
        }

        if (params != null) {
            String paramStr = URLEncodedUtils.format(params, HttpConstants.CHARSET);
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

            post.setEntity(new UrlEncodedFormEntity(params, HttpConstants.CHARSET));
            return post;
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    protected static HttpPost getHttpPost(String url, String content) {

        HttpPost post = new HttpPost(url);

        ContentType contentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), HttpConstants.CHARSET);
        post.setEntity(new StringEntity(content, contentType));

        return post;
    }
}
