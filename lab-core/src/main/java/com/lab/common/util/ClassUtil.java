package com.lab.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> void setValue(Class<T> clazz, T entity, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void setValue(T entity, String fieldName, Object value) {

        if (entity == null) {
            return;
        }

        try {
            Class<?> clazz = entity.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void setLabUserIdAndTime(T entity, Long userId) {
        long time = System.currentTimeMillis();
        setLabUserIdAndTime(entity, userId, time);
    }

    public static <T> void setLabUserIdAndTime(T entity, Long userId, Long time) {

        if (entity == null) {
            return;
        }

        try {
            Class<?> clazz = entity.getClass();

            Field id = clazz.getDeclaredField("id");
            id.setAccessible(true);

            if (id.get(entity) == null) {
                // 是 insert
                Field createTime = clazz.getDeclaredField("createTime");
                createTime.setAccessible(true);
                createTime.set(entity, time);

                Field createUserId = clazz.getDeclaredField("createLabUserId");
                createUserId.setAccessible(true);
                createUserId.set(entity, userId);
            }

            // 不管是 insert 还是 update 一定都会更新 updateTime
            Field updateTime = clazz.getDeclaredField("updateTime");
            updateTime.setAccessible(true);
            updateTime.set(entity, time);

            Field updateUserId = clazz.getDeclaredField("updateLabUserId");
            updateUserId.setAccessible(true);
            updateUserId.set(entity, userId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Object getValue(T entity, String fieldName){

        if (entity == null) {
            return null;
        }

        try {
            Class<?> clazz = entity.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> parseToMap(Object entity) {

        if (entity == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        try {
            Class<?> clazz = entity.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(entity));
            }

            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

}
