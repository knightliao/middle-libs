package com.github.knightliao.middle.msg.domain.consumer.handler;

import java.util.List;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:34
 */
public interface IMessageHandler {

    void register(String topic, IMessageProcessService processService);

    void process(String topic, List<String> rawDataList, List<Object> extMsgs);
}
