package com.github.knightliao.middle.msg.domain.consumer;

import java.util.List;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 16:55
 */
public interface IMessageListener {

    void onMessage(String topic, List<String> rawInstances, List<Object> extMsgs);
}
