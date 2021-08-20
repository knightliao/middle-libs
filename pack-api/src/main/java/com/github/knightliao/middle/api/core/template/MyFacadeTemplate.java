package com.github.knightliao.middle.api.core.template;

import com.github.knightliao.middle.api.core.callback.MyCallback;
import com.github.knightliao.middle.api.core.dto.BaseRequest;
import com.github.knightliao.middle.api.core.dto.BaseResponse;
import com.github.knightliao.middle.lang.constants.PackConstants;
import com.github.knightliao.middle.lang.exceptions.BizException;
import com.github.knightliao.middle.log.LoggerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:29
 */
@Slf4j
public class MyFacadeTemplate {

    public static <T> BaseResponse<T> execute(BaseRequest request, MyCallback<T> callback) {

        try {

            callback.checkParams();

            return callback.process();

        } catch (BizException bizException) {

            LoggerUtil.warn(log, "业务异常. request: %d %s", getBizErrorCode(bizException), request.toString());
            return BaseResponse.failWithParm(bizException.getMessage());

        } catch (Throwable throwable) {

            LoggerUtil.error(log, "系统异常. request: %d",
                    PackConstants.DEFAULT_ERROR_VALUE_INT, request.toString());
            return BaseResponse.failWithParm(throwable.toString());
        }
    }

    private static int getBizErrorCode(BizException bizException) {

        if (bizException == null) {
            return PackConstants.DEFAULT_ERROR_VALUE_INT;
        }

        return bizException.getBizErrorCode();
    }
}
