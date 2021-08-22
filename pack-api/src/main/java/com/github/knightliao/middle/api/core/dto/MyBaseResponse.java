package com.github.knightliao.middle.api.core.dto;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 13:37
 */
public class MyBaseResponse<T> {

    public static final int PARAM_ERROR = 400;
    public static final int SYS_ERROR = 500;
    public static final String SYS_ERROR_STRING = "system error";
    public static final int STATUS_OK = 1;

    @Getter
    private T data;
    @Getter
    private String message;
    @Getter
    private int status = STATUS_OK;

    private MyBaseResponse(T data) {
        this.data = data;
    }

    private MyBaseResponse() {

    }

    private MyBaseResponse(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public static <T> MyBaseResponse<T> success(T data) {
        return new MyBaseResponse<>(data);
    }

    public static <T> MyBaseResponse<T> success() {
        return new MyBaseResponse<T>();
    }

    public static <T> MyBaseResponse<T> fail(int status, String message) {
        return new MyBaseResponse<T>(status, message);
    }

    public static <T> MyBaseResponse<T> failWithParm(String message) {
        return new MyBaseResponse<T>(PARAM_ERROR, message);
    }

    public static <T> MyBaseResponse<T> failWithSys() {
        return new MyBaseResponse<T>(SYS_ERROR, SYS_ERROR_STRING);
    }

}
