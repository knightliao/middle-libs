package com.github.knightliao.middle.lang.exceptions.exceptions.others;

import com.github.knightliao.middle.lang.exceptions.enums.MyErrorExceptionEnum;
import com.github.knightliao.middle.lang.exceptions.exceptions.base.AbstractBaseException;

/**
 * 断言
 *
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/4 15:29
 */
public class NpeException extends AbstractBaseException {

    public NpeException(String message, Exception ex) {
        super(MyErrorExceptionEnum.NPE_ERROR, message, ex);
    }

    public NpeException(Exception ex) {
        super(MyErrorExceptionEnum.NPE_ERROR, ex);
    }
}
