package com.github.knightliao.middle.msg.support.helper;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 18:02
 */
public class KafkaDataSerializer implements Serializer<String> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, String s2) {

        return s2.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {

    }
}
