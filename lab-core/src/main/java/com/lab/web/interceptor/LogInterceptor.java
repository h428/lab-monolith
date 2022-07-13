package com.lab.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author hao
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 打印请求地址
        String info = "请求地址 : " + request.getRemoteAddr() + ":"
            + request.getRemotePort() + request.getRequestURI();
        log.debug(info);
        return true;
    }
}
