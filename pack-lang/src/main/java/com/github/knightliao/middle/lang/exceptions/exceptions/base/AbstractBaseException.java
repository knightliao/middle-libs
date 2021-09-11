package com.github.knightliao.middle.lang.exceptions.exceptions.base;

import com.github.knightliao.middle.lang.exceptions.enums.MyErrorExceptionEnum;

import lombok.Getter;

/**
 * 异常基类
 *
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 12:01
 */
public abstract class AbstractBaseException extends RuntimeException {

    @Getter
    protected String code;

    @Getter
    protected String msg;

    public AbstractBaseException(MyErrorExceptionEnum myErrorExceptionEnum, Exception ex) {
        super(myErrorExceptionEnum.getDesc(), ex);
        this.msg = myErrorExceptionEnum.getDesc();
        this.code = myErrorExceptionEnum.getKey();
    }

    public AbstractBaseException(MyErrorExceptionEnum myErrorExceptionEnum, Throwable throwable) {
        super(myErrorExceptionEnum.getDesc(), throwable);
        this.msg = myErrorExceptionEnum.getDesc();
        this.code = myErrorExceptionEnum.getKey();
    }

    public AbstractBaseException(MyErrorExceptionEnum myErrorExceptionEnum, String message, Exception ex) {
        super(message, ex);
        this.msg = message;
        this.code = myErrorExceptionEnum.getKey();
    }

    public AbstractBaseException(String message, Exception ex) {
        super(message, ex);
        this.msg = message;
    }

    public AbstractBaseException(String code, String message, Exception ex) {
        super(message, ex);
        this.msg = message;
        this.code = code;
    }

}
