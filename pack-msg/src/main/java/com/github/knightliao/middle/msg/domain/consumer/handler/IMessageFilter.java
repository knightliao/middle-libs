package com.github.knightliao.middle.msg.domain.consumer.handler;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:34
 */
public interface IMessageFilter {

    boolean isShouldDo(String topic, Object rawData);
}
