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
public class SocketRuntimeException extends AbstractBaseException {

    public SocketRuntimeException(String message, Exception ex) {
        super(MyErrorExceptionEnum.NET_SOCKET_ERROR, message, ex);
    }

    public SocketRuntimeException(Exception ex) {
        super(MyErrorExceptionEnum.NET_SOCKET_ERROR, ex);
    }

    public SocketRuntimeException(Throwable ex) {
        super(MyErrorExceptionEnum.NET_SOCKET_ERROR, ex);
    }

}
