package com.lab.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.lab.common.bean.PageBean;
import com.lab.common.builder.ObjectMapperBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonUtil {

    private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = ObjectMapperBuilder.builder()
            .indentOutput(true) // 为了方便 debug 和日志查阅，工具类开启格式化 json
            .build();
    }


    public static <T> String toJson(T bean) {
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            log.error("将 bean 转化为 json 时发生异常 : ", e);
            throw new RuntimeException(e);
        }
    }


    public static <T> T fromJsonToBean(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("从 json 反序列化为 bean 时发生异常 :", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> PageBean<T> fromJsonToPage(String json, Class<T> entityClass) {
        if (json == null) {
            return null;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory()
                    .constructParametricType(PageBean.class, entityClass);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("从 json 反序列化为 pageBean 时发生异常 :", e);
            throw new RuntimeException(e);
        }
    }

    // 使用 getTypeFactory
    public static <T> List<T> fromJsonToList(String json, Class<T> entityClass) {
        if (json == null) {
            return null;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory()
                    .constructParametricType(List.class, entityClass);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("从 json 反序列化为 listPage 时发生异常 :", e);
            throw new RuntimeException(e);
        }
    }

    // 使用 TypeReference
    public static <T> Set<T> fromJsonToSet(String json, Class<T> entityClass) {
        if (json == null) {
            return null;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory()
                    .constructParametricType(Set.class, entityClass);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("从 json 反序列化为 set 时发生异常 :", e);
            throw new RuntimeException(e);
        }
    }

    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyType,
            Class<V> entityClass) {

        if (json == null) {
            return null;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory()
                    .constructParametricType(HashMap.class, keyType, entityClass);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("从 json 反序列化为 map 时发生异常 :", e);
            throw new RuntimeException(e);
        }
    }

    public static <K, T> Map<K, List<T>> fromJsonToMapList(String json, Class<K> keyClass,
            Class<T> entityClass) {

        if (json == null) {
            return null;
        }

        try {
            JavaType keyType = objectMapper.getTypeFactory().constructType(keyClass);
            JavaType valueType = objectMapper.getTypeFactory()
                    .constructParametricType(List.class, entityClass);
            MapType mapType = objectMapper.getTypeFactory()
                    .constructMapType(HashMap.class, keyType, valueType);
            return objectMapper.readValue(json, mapType);
        } catch (IOException e) {
            log.error("从 json 反序列化为 mapList 时发生异常 :", e);
            throw new RuntimeException(e);
        }


    }

    public static <T> T fromJsonWithTypeReference(String json, TypeReference<T> typeReference) {

        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("从 json 通过 typeReference 反序列时发生异常 :", e);
            throw new RuntimeException(e);
        }
    }

    private JacksonUtil() {

    }

}
