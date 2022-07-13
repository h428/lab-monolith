package com.lab.business.threadlocal;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.lab.common.util.JacksonUtil;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerMapping;

/**
 * 本次请求相关的实验室 id，从请求对象 request 中分析并设置
 */
public final class LabIdThreadLocal {

    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    private LabIdThreadLocal() {

    }

    public static void set(Long labId) {
        LOCAL.set(labId);
    }

    public static Long get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }


    private static final String KEY = "labId";

    /**
     * 从请求对象分析并设置 labId，按序解析分别为：
     * 1. 占位符为 labId 的路径参数 /{labId}
     * 2. key 为 labId 的查询参数 ?labId=xxx
     *
     * @param request 请求对象
     * @return 解析后的 labId
     */
    @SuppressWarnings("unchecked")
    public static Long parseFromRequest(HttpServletRequest request) {
        // 取出占位符 map：spring 内部已经实现好了占位符处理并设置 Attribute
        Map<String, String> pathVariableMap = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String labIdPath = pathVariableMap == null ? null : pathVariableMap.get(KEY);

        // 使用路径参数中的 labId
        if (labIdPath != null) {
            long labId = Long.parseLong(labIdPath);
            set(labId);
            return labId;
        }

        // 使用查询参数中的 labId
        String labIdQuery = request.getParameter(KEY);
        if (labIdQuery != null) {
            long labId = Long.parseLong(labIdQuery);
            set(labId);
            return labId;
        }

        // 若为 application/json; 从请求体解析 labId
        String contentType = request.getContentType();

        if (!StrUtil.equals(ContentType.JSON.getValue(), contentType)) {
            return null;
        }

        String bodyString = getBodyString(request);

        if (StrUtil.isBlank(bodyString)) {
            return null;
        }

        Map<String, Object> bodyMap = JacksonUtil.fromJsonToMap(bodyString, String.class, Object.class);
        String labIdBody = (String)bodyMap.get(KEY);
        if (labIdBody != null) {
            long labId = Long.parseLong(labIdBody);
            set(labId);
            return labId;
        }

        // 全都解析不出则表示本次请求不和指定的 labId 相关
        return null;
    }

    static String getBodyString(HttpServletRequest request) {
        try {
            byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
            return new String(bodyBytes, request.getCharacterEncoding());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
