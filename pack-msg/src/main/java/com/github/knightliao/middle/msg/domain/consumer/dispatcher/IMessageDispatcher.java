package com.github.knightliao.middle.msg.domain.consumer.dispatcher;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;

import com.github.knightliao.middle.msg.domain.domain.KafkaOffset;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:01
 */
public interface IMessageDispatcher {

    Map<TopicPartition, KafkaOffset> dispatch(String topic, List<ConsumerRecord<String, String>> records);
}
