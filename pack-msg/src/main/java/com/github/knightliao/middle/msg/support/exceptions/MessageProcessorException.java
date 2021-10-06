package com.github.knightliao.middle.msg.support.exceptions;

import com.github.knightliao.middle.msg.support.enums.MessageErrorEnum;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/6 17:46
 */
public class MessageProcessorException extends RuntimeException {

    @Getter
    private int code = 400;

    public MessageProcessorException() {
        super();
    }

    public MessageProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageProcessorException(String message) {
        super(message);
    }

    public MessageProcessorException(String message, int code, Exception ex) {
        super(message, ex);
        this.code = code;
    }

    public MessageProcessorException(Throwable throwable) {
        super(throwable);
    }

    public static MessageProcessorException getMessageContentTypeError() {

        return new MessageProcessorException(MessageErrorEnum.TYPE_ERROR.getKey(),
                MessageErrorEnum.TYPE_ERROR.getValue(), null);
    }

    public static MessageProcessorException getMessageRetryError() {

        return new MessageProcessorException(MessageErrorEnum.RETRY.getKey(),
                MessageErrorEnum.RETRY.getValue(), null);
    }

    public static MessageProcessorException getMessageNoRetryError() {

        return new MessageProcessorException(MessageErrorEnum.NO_RETRY.getKey(),
                MessageErrorEnum.NO_RETRY.getValue(), null);
    }
}
