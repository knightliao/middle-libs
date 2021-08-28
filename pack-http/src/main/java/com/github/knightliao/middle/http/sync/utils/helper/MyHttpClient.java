package com.github.knightliao.middle.http.sync.utils.helper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 13:05
 */
@Slf4j
public class MyHttpClient extends MyAbstractHttpClient {

    public MyHttpClient(int total, int connPerRoute, String prefix) {
        super(total, connPerRoute, prefix);
    }
}
