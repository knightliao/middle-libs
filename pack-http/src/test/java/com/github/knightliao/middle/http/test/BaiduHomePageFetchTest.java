package com.github.knightliao.middle.http.test;

import org.junit.Assert;
import org.junit.Test;

import com.github.knightliao.middle.http.SimpleHttpSupport;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 14:39
 */
public class BaiduHomePageFetchTest {

    public static class BaiduHomePageFetcher extends SimpleHttpSupport {

        public BaiduHomePageFetcher(String apiHost, int numRetries) {
            init("baidu", apiHost, false, numRetries);
        }

        public String getContent() {

            String content = simpleHttpClient.get("/", 200, 200);

            return content;
        }
    }

    @Test
    public void test() {

        BaiduHomePageFetcher baiduHomePageFetcher = new BaiduHomePageFetcher("www.baidu.com:80", 3);
        String content = baiduHomePageFetcher.getContent();

        Assert.assertNotEquals(content, "");
    }
}
