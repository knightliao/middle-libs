package com.github.knightliao.middle.api.api.dto;

import lombok.Data;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 01:27
 */
@Data
public class BaseRpcResponse<T> {

    public static final int PARAM_ERROR = 400;
    public static final int SYS_ERROR = 500;
    public static final String SYS_ERROR_STRING = "system error";
    public static final int STATUS_OK = 1;

    private T data;
    private String message;
    private int status = STATUS_OK;

    private BaseRpcResponse(T data) {
        this.data = data;
    }

    private BaseRpcResponse() {

    }

    private BaseRpcResponse(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public static <T> BaseRpcResponse<T> success(T data) {
        return new BaseRpcResponse<T>(data);
    }

    public static <T> BaseRpcResponse<T> success() {
        return new BaseRpcResponse<T>();
    }

    public static <T> BaseRpcResponse<T> fail(int status, String message) {
        return new BaseRpcResponse<T>(status, message);
    }

    public static <T> BaseRpcResponse<T> failWithParm(String message) {
        return new BaseRpcResponse<>(PARAM_ERROR, message);
    }

    public static <T> BaseRpcResponse<T> failWithSys() {
        return new BaseRpcResponse<T>(SYS_ERROR, SYS_ERROR_STRING);
    }

}
