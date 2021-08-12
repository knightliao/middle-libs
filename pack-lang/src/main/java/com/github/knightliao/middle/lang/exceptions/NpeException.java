package com.github.knightliao.middle.lang.exceptions;

/**
 * 断言
 *
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/4 15:29
 */
public class NpeException extends RuntimeException {

    public NpeException() {
        super();
    }

    public NpeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NpeException(String message) {
        super(message);
    }

    public NpeException(Throwable throwable) {
        super(throwable);
    }
}
