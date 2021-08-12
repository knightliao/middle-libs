package com.github.knightliao.middle.lang.future;

import java.util.concurrent.TimeUnit;

import com.github.knightliao.middle.lang.exceptions.SocketRuntimeException;

/**
 * @author knightliao
 * @date 2021/8/4 15:34
 */
public interface IInvokeFuture {

    void addListener(IInvokeFutureListener listener);

    boolean isDone();

    Object getResult() throws SocketRuntimeException;

    void setResult(Object result);

    Object getResult(long timeout, TimeUnit unit);

    void setCause(Throwable cause);

    boolean isSuccess();

    Throwable getCause();
}
