package com.github.knightliao.middle.api.web.dto;

import lombok.Data;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 00:46
 */
@Data
public class BaseResponse<T> {

    public static final int PARAM_ERROR = 400;
    public static final int SYS_ERROR = 500;
    public static final String SYS_ERROR_STRING = "system error";
    public static final int STATUS_OK = 1;

    private T data;
    private String message;
    private int status = STATUS_OK;

    private BaseResponse(T data) {
        this.data = data;
    }

    private BaseResponse() {

    }

    private BaseResponse(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data);
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<T>();
    }

    public static <T> BaseResponse<T> fail(int status, String message) {
        return new BaseResponse<T>(status, message);
    }

    public static <T> BaseResponse<T> failWithParm(String message) {
        return new BaseResponse<T>(PARAM_ERROR, message);
    }

    public static <T> BaseResponse<T> failWithSys() {
        return new BaseResponse<T>(SYS_ERROR, SYS_ERROR_STRING);
    }

}
