package com.lab.web.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

/**
 * 处理 request 的请求体无法多次读取问题：为请求体的输入流添加装饰，缓存读取到的 body 并提供重复读取功能；
 * 那么如使该 Wrapper 生效呢：当前 Wrapper 本质上 HttpServletRequest 的一个装饰类（装饰模式），其覆盖某一方法的
 * 原有功能并提供额外功能，因此只需为 Wrapper 找到一个可以添加 HttpServletRequest 装饰类的入口即可；
 * 故常见有两种方式：
 *  1. 采用过滤器，在请求分发到 Servlet 之前，使用 Wrapper 替换原有的 Request（本项目采用）
 *  2. 自定义 DispatcherServlet 替换默认的 DispatcherServlet，并在调用 doDispatch 方法调用时添加装饰
 */
public class BodyCopyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存下来的 HTTP body
     */
    private final byte[] body;

    public BodyCopyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    /**
     * 重新包装输入流
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream bodyStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bodyStream.read();
            }

            /**
             * 下面的方法一般情况下不会被使用，如果你引入了一些需要使用ServletInputStream的外部组件，可以重点关注一下。
             * @return
             */
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        InputStream bodyStream = new ByteArrayInputStream(body);
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
