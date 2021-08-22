package com.github.knightliao.middle.lang.callback;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/22 18:40
 */
public interface IMyMethodCallback<T> {

    void preDo(T t);

    void afterDo(T t);
}
