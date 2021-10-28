package com.github.knightliao.middle.msg.impl.kafka.consumer.subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.middle.msg.domain.consumer.IMessageListener;
import com.github.knightliao.middle.msg.domain.consumer.dispatcher.IMessageDispatcher;
import com.github.knightliao.middle.msg.domain.consumer.dispatcher.MessageDispatcherImpl;
import com.github.knightliao.middle.msg.domain.consumer.subscribe.ISourceSubscribe;
import com.github.knightliao.middle.msg.domain.domain.KafkaOffset;
import com.github.knightliao.middle.msg.impl.kafka.consumer.config.KafkaConsumeConfig;
import com.github.knightliao.middle.msg.support.constants.LoggerNames;
import com.github.knightliao.middle.utils.thread.NamedThreadFactory;
import com.github.knightliao.middle.utils.thread.ThreadUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 16:57
 */
@Slf4j
public class KafkaSequenceSubscribe implements ISourceSubscribe {

    private static final Logger LOGGER_KAFKA = LoggerFactory.getLogger(LoggerNames.LOGGER_NAME);

    private KafkaConsumer<String, String> kafkaConsumer;

    private final String topicStr;
    private final String broker;

    // 处理组
    private final String consumerGroup;

    // 分配处理器
    private final IMessageDispatcher dispatcher;

    // 多少个consumer线程
    private ExecutorService executorService;

    private final List<String> topics = new ArrayList<>();

    // 处理方法
    private final ConsumeThread consumeThread = new ConsumeThread();

    private int pollTimeoutMs = 3000;
    private int printFrequency = 100;

    public KafkaSequenceSubscribe(int consumerNum, String consumerName,
                                  String topicStr, String broker,
                                  String consumerGroup, IMessageListener messageListener,
                                  int pollTimeoutMs, int printFrequency) {

        this.topicStr = topicStr;
        this.broker = broker;
        this.consumerGroup = consumerGroup;
        this.dispatcher = new MessageDispatcherImpl(messageListener);
        this.pollTimeoutMs = pollTimeoutMs;
        this.printFrequency = printFrequency;

        this.executorService = new ThreadPoolExecutor(consumerNum, consumerNum, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(consumerNum), new NamedThreadFactory(consumerName));
    }

    @Override
    public void start() {

        log.info("subscriptions: {}", topicStr);

        //
        kafkaConsumer = KafkaConsumeConfig.build(broker, consumerGroup);

        //
        topics.addAll(Splitter.on(",")
                .omitEmptyStrings()
                .trimResults()
                .splitToList(topicStr));
        kafkaConsumer.subscribe(topics);

        //
        executorService.execute(consumeThread);
    }

    @Override
    public void shutdown() {
        consumeThread.shutdown();
    }

    private class ConsumeThread implements Runnable {

        private volatile boolean running;

        @Override
        public void run() {

            running = true;

            while (running) {

                ConsumerRecords<String, String> consumerRecords;

                try {
                    consumerRecords = kafkaConsumer.poll(pollTimeoutMs);

                } catch (Exception ex) {

                    log.error("kafka pool error", ex);
                    ThreadUtil.sleep(1000);
                    continue;
                }

                //
                ListMultimap<String, ConsumerRecord<String, String>> topicRecordMap = ArrayListMultimap.create();
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    topicRecordMap.put(consumerRecord.topic(), consumerRecord);
                }

                consume(topicRecordMap);
            }
        }

        private void consume(ListMultimap<String, ConsumerRecord<String, String>> topicRecordMap) {

            while (running) {

                String currentTopic = "";
                int currentSize = 0;
                long start = 0;

                try {

                    Map<TopicPartition, KafkaOffset> offsetMap = new HashMap<>(topicRecordMap.size());

                    for (String topic : topicRecordMap.keys()) {

                        currentTopic = topic;
                        start = System.nanoTime();

                        //
                        List<ConsumerRecord<String, String>> consumerRecords = topicRecordMap.get(topic);
                        currentSize = consumerRecords.size();

                        //
                        Map<TopicPartition, KafkaOffset> resultMap = dispatcher.dispatch(topic, consumerRecords);
                        offsetMap.putAll(resultMap);

                        //
                        doKafkaReceiveLog(currentTopic, currentSize, start, true, 0);
                    }

                    if (offsetMap.size() > 0) {
                        commit(offsetMap);
                    }

                    return;

                } catch (CommitFailedException ex) {

                    doKafkaReceiveLog(currentTopic, currentSize, start, false, 1);
                    log.error("kafka commit record fail ", ex);

                } catch (Throwable ex) {

                    doKafkaReceiveLog(currentTopic, currentSize, start, false, 2);
                    log.error("consume record fail ", ex);
                    ThreadUtil.sleep(1000);
                }
            }
        }

        private void commit(Map<TopicPartition, KafkaOffset> offsetMap) {

            Map<TopicPartition, OffsetAndMetadata> offsetAndMetadataMap = Maps.newHashMap();
            for (TopicPartition partition : offsetMap.keySet()) {

                KafkaOffset offset = offsetMap.get(partition);

                OffsetAndMetadata metadata = new OffsetAndMetadata(offset.getOffset(),
                        String.valueOf(System.currentTimeMillis()));
                offsetAndMetadataMap.put(partition, metadata);

                //
                doOffsetLog(partition.topic(), offset.getPartition().partition(), offset.getOffset());
            }

            kafkaConsumer.commitSync(offsetAndMetadataMap);
        }

        private void shutdown() {
            running = false;
        }
    }

    private void doKafkaReceiveLog(String topic, int length, long startTime, boolean status, int statusCode) {
        LOGGER_KAFKA.info("KAFKA_RECEIVE {} {} {} {} {}", topic, length, System.nanoTime() - startTime, status,
                statusCode);
    }

    private void doOffsetLog(String topic, int partition, long offset) {

        if (offset % this.printFrequency == 0) {
            LOGGER_KAFKA.info("KAFKA_OFFSET {} {} {}", topic, partition, offset);
        }
    }
}
