package com.lab.common.exception;

/**
 * 业务异常，大多是参数输入有误，因此暂用 400 状态码
 */
public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(400, message);
    }
}
