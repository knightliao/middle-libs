package com.github.knightliao.middle.idem.exception;

import com.github.knightliao.middle.idem.support.enums.IdemErrorEnum;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/12 14:29
 */
public class IdemException extends RuntimeException {

    private int code;
    private String msg;

    public IdemException(String message, int code, Exception ex) {
        super(message, ex);
        this.msg = message;
        this.code = code;
    }

    public IdemException(IdemErrorEnum idemErrorEnum, Exception ex) {
        super(idemErrorEnum.getDesc(), ex);
        this.msg = idemErrorEnum.getDesc();
        this.code = idemErrorEnum.getValue();
    }

    // 重复异常
    public static IdemException getIdemDuplicationException() {
        return new IdemException(IdemErrorEnum.DUPLICATION, null);
    }

    // 并发处理
    public static IdemException getIdemConcurrentException() {
        return new IdemException(IdemErrorEnum.CONCURRENT, null);
    }
}
