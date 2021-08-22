package com.github.knightliao.middle.api.core.callback;

import com.github.knightliao.middle.api.core.dto.MyBaseResponse;
import com.github.knightliao.middle.lang.exceptions.BizException;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:26
 */
public interface IMyRequesCallback<T> {

    void checkParams() throws BizException;

    MyBaseResponse<T> process();
}
