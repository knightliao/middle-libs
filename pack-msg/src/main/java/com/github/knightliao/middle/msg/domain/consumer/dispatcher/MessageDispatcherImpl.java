package com.github.knightliao.middle.msg.domain.consumer.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;

import com.github.knightliao.middle.msg.domain.consumer.IMessageListener;
import com.github.knightliao.middle.msg.domain.domain.KafkaOffset;
import com.google.common.base.Preconditions;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/28 23:06
 */
public class MessageDispatcherImpl implements IMessageDispatcher {

    private final IMessageListener messageListener;

    public MessageDispatcherImpl(IMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public Map<TopicPartition, KafkaOffset> dispatch(String topic, List<ConsumerRecord<String, String>> records) {

        List<String> list = records.stream().map(ConsumerRecord::value).collect(Collectors.toList());

        List<Object> extMsgs = new ArrayList<>();
        extMsgs.addAll(records);

        messageListener.onMessage(topic, list, extMsgs);

        //
        Map<TopicPartition, KafkaOffset> result = new HashMap<>(records.size());
        for (ConsumerRecord<String, String> consumerRecord : records) {

            //
            TopicPartition partition = new TopicPartition(consumerRecord.topic(), consumerRecord.partition());
            long timestamp = consumerRecord.timestamp();

            //
            long offset = consumerRecord.offset() + 1;

            //
            KafkaOffset kafkaOffset =
                    KafkaOffset.builder().partition(partition).offset(offset).timestamp(timestamp).build();
            KafkaOffset oldOffset = result.put(partition, kafkaOffset);

            if (oldOffset != null) {
                Preconditions.checkState(oldOffset.getOffset() <= offset);
            }
        }

        return result;
    }
}
