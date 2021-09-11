package com.github.knightliao.middle.lang.exceptions.exceptions.param;

import com.github.knightliao.middle.lang.exceptions.enums.MyErrorExceptionEnum;
import com.github.knightliao.middle.lang.exceptions.exceptions.base.AbstractBaseException;

/**
 * 幂等异常
 *
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/12 14:29
 */
public class IdemException extends AbstractBaseException {

    public IdemException(MyErrorExceptionEnum myErrorExceptionEnum, String message, Exception ex) {
        super(myErrorExceptionEnum, message, ex);
    }

    public IdemException(MyErrorExceptionEnum myErrorExceptionEnum, Exception ex) {
        super(myErrorExceptionEnum, ex);
    }

    // 重复异常
    public static IdemException getIdemDuplicationException() {
        return new IdemException(MyErrorExceptionEnum.PARAM_IDEM_DUPLICATION, null);
    }

    // 并发处理
    public static IdemException getIdemConcurrentException() {
        return new IdemException(MyErrorExceptionEnum.PARAM_IDEM_CONCURRENT, null);
    }
}
