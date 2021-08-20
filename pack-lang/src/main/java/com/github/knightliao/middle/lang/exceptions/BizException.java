package com.github.knightliao.middle.lang.exceptions;

import com.github.knightliao.middle.lang.exceptions.enums.MyErrorExceptionEnum;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 12:33
 */
public class BizException extends RuntimeException {

    @Getter
    private String errorCode;
    @Getter
    private int bizErrorCode;
    @Getter
    private String message;

    public BizException(String errorCode, String message, Exception ex) {
        super(message, ex);
        this.errorCode = errorCode;
        this.message = message;
    }

    public static BizException getParamError(int bizErrorCode, String message) {

        BizException bizException = new BizException(MyErrorExceptionEnum.ILLEGAL_ARGUMENT.getKey(),
                message, null);

        bizException.bizErrorCode = bizErrorCode;
        return bizException;
    }

    public static BizException getParamError(String message, Exception ex) {

        return new BizException(MyErrorExceptionEnum.ILLEGAL_ARGUMENT.getKey(),
                message, ex);
    }
}
