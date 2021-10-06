package com.github.knightliao.middle.msg.domain.consumer.subscribe;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 16:56
 */
public interface ISourceSubscribe {

    void start();

    void shutdown();
}
