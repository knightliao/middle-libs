package com.github.knightliao.middle.msg.impl.kafka.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;

import com.github.knightliao.middle.msg.support.helper.KafkaDataSerializer;
import com.github.knightliao.middle.msg.support.helper.KafkaRouteSerializer;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 18:00
 */
public class KafkaProducerUtils {

    public static KafkaProducer buildKafkaProducer(String broker) {

        Map<String, Object> configs = new HashMap<>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        configs.put("acks", "1");
        configs.put("retries", "0");
        configs.put("match.size", "2097152");
        configs.put("buffer.memory", "33554432");

        configs.put("key.serializer", KafkaRouteSerializer.class.getName());
        configs.put("value.serializer", KafkaDataSerializer.class.getName());

        return new KafkaProducer(configs);
    }
}
