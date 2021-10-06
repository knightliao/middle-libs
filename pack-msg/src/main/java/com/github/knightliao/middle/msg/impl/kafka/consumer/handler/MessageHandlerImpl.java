package com.github.knightliao.middle.msg.impl.kafka.consumer.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.middle.msg.domain.consumer.handler.IMessageHandler;
import com.github.knightliao.middle.msg.domain.consumer.handler.IMessageProcessService;
import com.github.knightliao.middle.msg.support.constants.LoggerNames;
import com.github.knightliao.middle.msg.support.enums.MessageErrorEnum;
import com.github.knightliao.middle.msg.support.enums.MessageProcessStatusEnum;
import com.github.knightliao.middle.msg.support.exceptions.MessageProcessorException;
import com.github.knightliao.middle.utils.trans.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:37
 */
@Slf4j
public class MessageHandlerImpl implements IMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoggerNames.LOGGER_PROCESSOR_LOG);

    private Map<String, IMessageProcessService> processServiceMap = new HashMap<>();

    @Override
    public void register(String topic, IMessageProcessService processService) {

        processServiceMap.put(topic, processService);
    }

    @Override
    public void process(String topic, List<String> rawDataList, List<Object> extMsgs) {

        IMessageProcessService processService = processServiceMap.get(topic);

        //
        if (processService == null) {
            log.error("cannot find message handler {}", topic);
        }

        //
        int index = 0;
        for (String rawData : rawDataList) {

            boolean status = true;
            int errorCode = MessageProcessStatusEnum.NORMAL.getValue();

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            boolean isOk = true;

            Object extMsg = null;
            if (index < extMsgs.size()) {
                extMsg = extMsgs.get(index);
            }

            try {

                Object object = decode(processService, rawData);
                if (object == null) {
                    log.error("decode error {}", rawData);
                    isOk = false;
                    errorCode = MessageProcessStatusEnum.DECODE_ERROR.getValue();

                } else {

                    try {

                        isOk = processService.process(topic, object, extMsg);

                    } catch (MessageProcessorException ex) {

                        if (ex.getCode() != MessageErrorEnum.NO_RETRY.getValue()) {
                            // 进行重试
                            errorCode = MessageProcessStatusEnum.RETRY.getValue();
                            log.error(ex.toString(), ex);
                            status = false;
                            throw MessageProcessorException.getMessageNoRetryError();

                        } else {

                            // 不需要重试, 忽略
                            errorCode = MessageProcessStatusEnum.IGNORE.getValue();
                            log.warn(ex.toString(), ex);
                        }

                    } catch (Exception ex) {

                        // 需要重试
                        status = false;
                        errorCode = MessageProcessStatusEnum.ERROR.getValue();
                        log.error(ex.toString(), ex);
                        throw MessageProcessorException.getMessageRetryError();
                    }
                }
            } finally {

                doLog(topic, rawData, extMsg, status, errorCode, isOk, stopWatch);
                index++;
            }
        }
    }

    private Object decode(IMessageProcessService messageProcessService, String rawData) {

        try {

            Class<?> classType = messageProcessService.getClassType();
            if (classType == null) {
                return rawData;
            }

            return JsonUtils.fromJson(rawData, classType);

        } catch (Exception ex) {

            return null;
        }
    }

    private void doLog(String topic, String rawData, Object extMsg, boolean status, int errorCode, boolean isOk,
                       StopWatch stopWatch) {

        long delay = -1;
        if (extMsg instanceof ConsumerRecord) {
            ConsumerRecord<String, String> consumerRecord = (ConsumerRecord<String, String>) extMsg;
            delay = System.currentTimeMillis() - consumerRecord.timestamp();
        }

        stopWatch.stop();
        logger.info("{} {} {} {} {} {} {}", topic, status, errorCode, stopWatch.getTime(), isOk, delay, rawData);
    }
}
