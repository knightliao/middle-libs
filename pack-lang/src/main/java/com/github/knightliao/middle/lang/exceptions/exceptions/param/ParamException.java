package com.github.knightliao.middle.lang.exceptions.exceptions.param;

import com.github.knightliao.middle.lang.exceptions.enums.MyErrorExceptionEnum;
import com.github.knightliao.middle.lang.exceptions.exceptions.base.AbstractBaseException;

import lombok.Getter;

/**
 * 参数异常
 *
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 11:51
 */
public class ParamException extends AbstractBaseException {

    @Getter
    private String field;

    public ParamException(String field, String message, Exception ex) {
        super(MyErrorExceptionEnum.PARAM_ERROR, message, ex);
        this.field = field;
    }

    public ParamException(String field, Exception ex) {
        super(MyErrorExceptionEnum.PARAM_ERROR, ex);
        this.field = field;
    }

    public ParamException(String field) {
        super(MyErrorExceptionEnum.PARAM_ERROR, null);
        this.field = field;
    }

}
