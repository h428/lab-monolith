package com.lab.web.interceptor;

import com.google.common.net.HttpHeaders;
import com.lab.business.threadlocal.BaseUserIdThreadLocal;
import com.lab.business.threadlocal.LabIdThreadLocal;
import com.lab.common.bean.ResBean;
import com.lab.common.component.TokenUtil;
import com.lab.common.util.AjaxUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 对 option 请求放行，用于 ajax 跨域
        String method = request.getMethod();
        if (HttpMethod.OPTIONS.name().equals(method)) {
            return true;
        }



        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        Long baseUserId = TokenUtil.isBaseUserLogin(token);

        // redis 不存在对应的 token，表示未登录
        if (baseUserId == null) {
            // 特别注意，采用直接抛出异常的做法，若是用户访问的 url 为非法 url（Controller 中没有声明），
            // 该异常将无法被全局异常处理器处理而会被直接抛出到用户
            // throw new BaseException(HttpStatus.HTTP_UNAUTHORIZED, "未登录");

            // 因此对于全局过滤器，建议的做法是直接回写响应而不要利用全局异常
            // 这样，即使访问的是 Controller 中没有声明的 url，也会被登录拦截器过滤并正确给出 json 结果
            ResBean<Object> unauthorized = ResBean.unauthorized_401("请重新登录");
            AjaxUtil.writeObject(response, 200, unauthorized);
            return false;
        }

        BaseUserIdThreadLocal.set(baseUserId);

        // 从请求对象解析 labId
        LabIdThreadLocal.parseFromRequest(request);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseUserIdThreadLocal.remove();
    }
}
