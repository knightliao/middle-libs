package com.github.knightliao.middle.http.test.async;

import org.junit.Test;

import com.github.knightliao.middle.http.async.client.ISimpleAsyncHttpClient;
import com.github.knightliao.middle.http.async.client.impl.SimpleAsyncHttpClientDefaultImpl;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/27 00:18
 */
public class AsyncHttpTestCase {

    @Test
    public void test() {

        ISimpleAsyncHttpClient iSimpleHttpAsyncClient = new SimpleAsyncHttpClientDefaultImpl();
    }
}
