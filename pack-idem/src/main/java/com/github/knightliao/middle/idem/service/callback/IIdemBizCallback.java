package com.github.knightliao.middle.idem.service.callback;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/12 14:26
 */
public interface IIdemBizCallback<T> {

    // 执行
    T process();
}
