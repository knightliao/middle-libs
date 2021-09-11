package com.github.knightliao.middle.api.core.template;

import com.github.knightliao.middle.api.core.callback.IMyRequesCallback;
import com.github.knightliao.middle.api.core.dto.MyBaseRequest;
import com.github.knightliao.middle.api.core.dto.MyBaseResponse;
import com.github.knightliao.middle.lang.constants.PackConstants;
import com.github.knightliao.middle.lang.exceptions.exceptions.biz.BizException;
import com.github.knightliao.middle.log.LoggerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:29
 */
@Slf4j
public class MyFacadeTemplate {

    public static <T> MyBaseResponse<T> execute(MyBaseRequest request, IMyRequesCallback<T> callback) {

        try {

            callback.checkParams();

            return callback.process();

        } catch (BizException bizException) {

            LoggerUtil.warn(log, "业务异常. request: {0} {1}",
                    getBizErrorCode(bizException), request.toString());
            return MyBaseResponse.failWithParm(bizException.getMessage());

        } catch (Throwable throwable) {

            LoggerUtil.error(log, "系统异常. request: {0} {1}",
                    PackConstants.DEFAULT_ERROR_VALUE_INT, request.toString());
            return MyBaseResponse.failWithParm(throwable.toString());
        }
    }

    private static String getBizErrorCode(BizException bizException) {

        if (bizException == null) {
            return PackConstants.DEFAULT_ERROR_VALUE_STRING;
        }

        return bizException.getBizErrorCode();
    }
}
