package com.github.knightliao.middle.lang.future;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/4 15:34
 */
public interface IInvokeFutureListener {

    void operationComplete(IInvokeFuture future) throws Exception;
}
