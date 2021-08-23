package com.github.knightliao.middle.http.impl.helper;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 12:21
 */
public interface IFunctionWrapper {

    CloseableHttpResponse execute(String url) throws IOException;
}
