package com.lab.common.exception;

public class NoPermissionException extends BaseException {

    public NoPermissionException() {
        this("权限不足");
    }

    public NoPermissionException(String message) {
        super(403, message);
    }
}
