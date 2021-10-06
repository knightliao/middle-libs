package com.github.knightliao.middle.msg.domain.consumer.handler;

import com.github.knightliao.middle.msg.support.exceptions.MessageProcessorException;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:35
 */
public interface IMessageProcessService {

    boolean process(String topic, Object object, Object extMap) throws MessageProcessorException;

    Class<?> getClassType();
}
