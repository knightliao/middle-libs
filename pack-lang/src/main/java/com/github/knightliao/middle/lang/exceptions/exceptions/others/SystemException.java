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
public class SystemException extends AbstractBaseException {

    public SystemException(String message, Exception ex) {
        super(MyErrorExceptionEnum.SYSTEM_ERROR, message, ex);
    }

    public SystemException(Exception ex) {
        super(MyErrorExceptionEnum.SYSTEM_ERROR, ex);
    }
}
