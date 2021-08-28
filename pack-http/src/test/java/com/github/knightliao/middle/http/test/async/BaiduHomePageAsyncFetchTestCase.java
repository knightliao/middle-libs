package com.github.knightliao.middle.http.test.async;

import java.util.concurrent.CompletionStage;

import org.junit.Assert;
import org.junit.Test;

import com.github.knightliao.middle.http.async.utils.MyAsyncHttpUtils;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 14:39
 */
public class BaiduHomePageAsyncFetchTestCase {

    @Test
    public void test() {

        CompletionStage<String> content = MyAsyncHttpUtils.get("http://www.baidu.com", 5000);

        try {

            String data = content.toCompletableFuture().get();
            Assert.assertNotEquals(data, "");

        } catch (Exception e) {

            System.out.println(e.toString());
            Assert.fail();
        }
    }
}
