package com.lab.web.filter;

import com.lab.web.wrapper.BodyCopyHttpServletRequestWrapper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@WebFilter(filterName = "requestBodyCopyFilter", urlPatterns = "/*")
@Order(1)
public class RequestBodyCopyFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String method = httpServletRequest.getMethod();
            String contentType = httpServletRequest.getContentType() == null ? "" : httpServletRequest.getContentType();
            // 如果不是文件上传
            // 如果要严格的过滤，可以考虑只有 json 的时候才拷贝
            if (!contentType.equals(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                // 重新生成 ServletRequest 这个新的 ServletRequest 获取流时会将流的数据重写进流里面
                requestWrapper = new BodyCopyHttpServletRequestWrapper((HttpServletRequest) request);
            }
        }
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
