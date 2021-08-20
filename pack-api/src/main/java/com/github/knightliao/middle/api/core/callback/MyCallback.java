package com.github.knightliao.middle.api.core.callback;

import com.github.knightliao.middle.api.core.dto.BaseResponse;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:26
 */
public interface MyCallback<T> {

    void checkParams();

    BaseResponse<T> process();
}
