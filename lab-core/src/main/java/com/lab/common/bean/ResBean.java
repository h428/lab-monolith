package com.lab.common.bean;

import com.lab.common.constant.MyHttpMessage;
import com.lab.common.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 自定义响应包括类
 * @param <T> 数据类型
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class ResBean<T> {

    private int status; // 状态码

    private String message; // 提示信息

    private T data; // 数据


    public T checkAndGetData() {
        if (this.data == null) {
            throw new BaseException(this.status, this.message);
        }
        return this.data;
    }

    // 给定状态码和提示消息构建响应体
    public static <T> ResBean<T> build(int status, String message, T data) {
        return ResBean.<T>builder().status(status).message(message).data(data).build();
    }

    // 200
    public static <T> ResBean<T> ok_200(String message, T data) {
        return ResBean.build(OK, message, data);
    }

    public static <T> ResBean<T> ok_200(String message) {
        return ResBean.build(OK, message, null);
    }

    public static <T> ResBean<T> ok_200(T data) {
        return ResBean.build(OK, MyHttpMessage.OK, data);
    }


    public static <T> ResBean<T> ok_200() {
        return ResBean.build(OK, MyHttpMessage.OK, null);
    }


    // 400
    public static <T> ResBean<T> badRequest_400(String message) {
        return ResBean.build(BAD_REQUEST, message, null);
    }

    public static <T> ResBean<T> badRequest_400() {
        return ResBean.build(BAD_REQUEST, MyHttpMessage.BAD_REQUEST, null);
    }

    // 401
    public static <T> ResBean<T> unauthorized_401(String message) {
        return ResBean.build(UNAUTHORIZED, message, null);
    }

    public static <T> ResBean<T> unauthorized_401() {
        return ResBean.build(UNAUTHORIZED, MyHttpMessage.UNAUTHORIZED, null);
    }

    // 403
    public static <T> ResBean<T> forbidden_403(String message) {
        return ResBean.build(FORBIDDEN, message, null);
    }

    public static <T> ResBean<T> forbidden_403() {
        return ResBean.build(FORBIDDEN, MyHttpMessage.FORBIDDEN, null);
    }

    // 404
    public static <T> ResBean<T> not_found_404(String msg) {
        return ResBean.build(NOT_FOUND, MyHttpMessage.NOT_FOUND, null);
    }

    public static <T> ResBean<T> not_found_404() {
        return ResBean.build(NOT_FOUND, MyHttpMessage.NOT_FOUND, null);
    }


    // 409
    public static <T> ResBean<T> conflict_409(String msg) {
        return ResBean.build(CONFLICT, MyHttpMessage.CONFLICT, null);
    }

    public static <T> ResBean<T> conflict_409() {
        return ResBean.build(CONFLICT, MyHttpMessage.CONFLICT, null);
    }


    // 500
    public static <T> ResBean<T> internal_server_error_500(String msg) {
        return ResBean
            .build(INTERNAL_SERVER_ERROR, MyHttpMessage.INTERNAL_SERVER_ERROR, null);
    }

    public static <T> ResBean<T> internal_server_error_500() {
        return ResBean
            .build(INTERNAL_SERVER_ERROR, MyHttpMessage.INTERNAL_SERVER_ERROR, null);
    }
    
    
    // 常用通用状态码
    private static final int OK = 200;
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int CONFLICT = 409;
    private static final int INTERNAL_SERVER_ERROR = 500;
    
}
