package com.lab.common.constant;

/**
 * @author hao
 */
public interface MyHttpStatus {
    int OK = 200;
    int OK_NO_MESSAGE = 2000; // 自定义状态，用于 client 查询记录类不弹出体制
    int CREATED = 201;
    int ACCEPTED = 202;
    int NOT_AUTHORITATIVE = 203;
    int NO_CONTENT = 204;
    int RESET = 205;
    int PARTIAL = 206;
    int MULTI_CHOICE = 300;
    int MOVED_PERM = 301;
    int MOVED_TEMP = 302;
    int SEE_OTHER = 303;
    int NOT_MODIFIED = 304;
    int USE_PROXY = 305;
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int PAYMENT_REQUIRED = 402;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int BAD_METHOD = 405;
    int NOT_ACCEPTABLE = 406;
    int PROXY_AUTH = 407;
    int CLIENT_TIMEOUT = 408;
    int CONFLICT = 409;
    int GONE = 410;
    int LENGTH_REQUIRED = 411;
    int PRECON_FAILED = 412;
    int ENTITY_TOO_LARGE = 413;
    int REQ_TOO_LONG = 414;
    int UNSUPPORTED_TYPE = 415;
    int INTERNAL_SERVER_ERROR = 500;
    int NOT_IMPLEMENTED = 501;
    int BAD_GATEWAY = 502;
    int UNAVAILABLE = 503;
    int GATEWAY_TIMEOUT = 504;
    int VERSION = 505;
}
