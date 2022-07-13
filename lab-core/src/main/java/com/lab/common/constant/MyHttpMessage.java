package com.lab.common.constant;

public interface MyHttpMessage {
    String OK = "请求成功";
    String BAD_REQUEST = "请求参数有误";
    String UNAUTHORIZED = "未登录";
    String FORBIDDEN = "权限不足";
    String NOT_FOUND = "资源不存在";
    String CONFLICT = "发生冲突，请求失败";
    String INTERNAL_SERVER_ERROR = "服务器内部异常";
    String TOKEN_NOT_EMPTY = "token 不能为空";
    String TOKEN_INVALID = "token 过期或非法";

    // 通用业务提示信息
    String ADD_RECORD_SUCCESS = "保存记录成功";
    String ADD_RECORD_CONFLICT_ERROR = "发生冲突，记录保存失败";
    String DELETE_RECORD_SUCCESS = "删除记录成功";
    String UPDATE_RECORD_SUCCESS = "更新记录成功";
    String RECORD_NOT_FOUND = "记录不存在";
}
