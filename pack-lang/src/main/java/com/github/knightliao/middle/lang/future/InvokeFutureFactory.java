package com.github.knightliao.middle.lang.future;

import com.github.knightliao.middle.lang.future.impl.InvokeFutureImpl;

/**
 * @author knightliao
 * @date 2021/8/4 15:40
 */
public class InvokeFutureFactory {

    public static IInvokeFuture getInvokeFutureDefaultImpl() {

        return new InvokeFutureImpl();
    }
}
