package com.github.knightliao.middle.lang.exceptions.exceptions.biz;

import com.github.knightliao.middle.lang.exceptions.enums.MyErrorExceptionEnum;
import com.github.knightliao.middle.lang.exceptions.exceptions.base.AbstractBaseException;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:33
 */
public class BizException extends AbstractBaseException {

    @Getter
    private String bizErrorCode;

    public BizException(MyErrorExceptionEnum myErrorExceptionEnum, String bizCode, String message, Exception ex) {
        super(myErrorExceptionEnum, message, ex);
        this.bizErrorCode = bizCode;
    }

    public BizException(MyErrorExceptionEnum myErrorExceptionEnum, String bizCode, Exception ex) {
        super(myErrorExceptionEnum, ex);
        this.bizErrorCode = bizCode;
    }

    public static BizException getParamError(String bizErrorCode, String message) {

        return new BizException(MyErrorExceptionEnum.PARAM_ERROR, bizErrorCode,
                message, null);
    }

    public static BizException getParamError(String message, Exception ex) {
        return new BizException(MyErrorExceptionEnum.PARAM_ERROR, "", message, ex);
    }

    public static BizException getParamError(String bizErrorCode) {
        return new BizException(MyErrorExceptionEnum.PARAM_ERROR, "", null);
    }
}
