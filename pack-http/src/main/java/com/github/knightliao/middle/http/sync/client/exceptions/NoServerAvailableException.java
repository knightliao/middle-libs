package com.github.knightliao.middle.http.sync.client.exceptions;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 12:41
 */
public class NoServerAvailableException extends RuntimeException {

    public NoServerAvailableException() {
        super();
    }

    public NoServerAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoServerAvailableException(String message) {
        super(message);
    }

    public NoServerAvailableException(Throwable throwable) {
        super(throwable);
    }
}
