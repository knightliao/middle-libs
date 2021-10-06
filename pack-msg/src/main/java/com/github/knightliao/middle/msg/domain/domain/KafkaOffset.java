package com.github.knightliao.middle.msg.domain.domain;

import org.apache.kafka.common.TopicPartition;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:02
 */
@Data
@Builder
public class KafkaOffset {

    private TopicPartition partition;
    private long offset;
    private long timestamp;

    @Tolerate
    public KafkaOffset() {
    }
}
