package com.lab.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 实体工具类，通过反射打印实体和生成实例
 */
public class EntityUtil {

    // 不想随机生成值的列，比如 deleteTime, 外键等，因为这些列不能生成随机值，需要设置为 null
    private static Set<String> EXCLUDE_FIELD = new HashSet<>();

    static {
//        EXCLUDE_FIELD.add("constant");
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "null object";
        }

        try {
            // 首先获取对象的所有域，并确定他们的名称
            Class<?> objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();

            if (objClass == String.class) {
                return (String) obj;
            }

            // 添加类名和左括号
            StringBuilder sb = new StringBuilder();
            sb.append(objClass.getSimpleName()).append("{");
            // 取出域，拼接起来
            for (Field field : fields) {
                String fieldName = field.getName();
                field.setAccessible(true);
                sb.append(fieldName).append("=").append(field.get(obj)).append(", ");
            }
            // 删除最后两个字符并打上右括号
            sb.delete(sb.length() - 2, sb.length());
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toString(Collection<?> objs) {

        if (objs == null) {
            return "[null listPage]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("the size is ").append(objs.size()).append(", data is [\n");

        for (Object obj : objs) {
            sb.append(toString(obj)).append("\n");
        }
        return sb.append("]").toString();
    }

    public static <K, V> String toString(Map<K, V> map) {

        if (map == null) {
            return "{null map}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("the size is ").append(map.size()).append(", data is {\n");

        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append("{ key = ").append(entry.getKey())
                    .append(", value = ").append(entry.getValue())
                    .append(" }\n");
        }
        return sb.append("}").toString();
    }

    /**
     * 利用反射技术，输出对象的字符串，这样就不必为每个对象编写 toString 方法，方便开发
     *
     * @param obj 要打印的对象
     */
    public static void printString(Object obj) {
        System.out.println(toString(obj));
    }

    /**
     * 利用反射技术，输出集合中每个对象的字符串，这样就不必为每个对象编写 toString 方法，方便开发
     *
     * @param objs 集合
     */
    public static void printString(Collection<?> objs) {
        System.out.println(toString(objs));
    }

    public static <K, V> void printString(Map<K, V> map) {
        System.out.println(toString(map));
    }


    public static <T> T generateRandomOne(Class<T> clazz) {
        int num = (int) (Math.random() * 100); // 随机数
        return generateRandomOne(clazz, num);
    }

    /**
     * 利用反射技术，随机生成对象实例，其中 Id 列不设置，String 类型的列设置：为列名+随机数，整型直接设置为随机数
     *
     * @param clazz 目标类型的 Class
     * @param <T>   泛型 T，即目标对象类型
     * @return 返回生成的实例
     */
    public static <T> T generateRandomOne(Class<T> clazz, int num) {
        try {
            T obj = clazz.newInstance();

            Field[] declaredFields = clazz.getDeclaredFields();

            for (Field field : declaredFields) {

                String fieldName = field.getName();
                Class<?> fieldType = field.getType();

                // serialVersionUID 列跳过生成值
                if (field.getName().equals("serialVersionUID") || EXCLUDE_FIELD.contains(fieldName)) {
                    continue;
                }

                // 跳过 static 域
                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }

                field.setAccessible(true);

                // 其他列，若是 String 类型，则根据 列名+随机数 进行赋值
                if (fieldType == String.class) {
                    // 字符串类型设置为：列名+随机数
                    field.set(obj, field.getName() + num);
                } else if (fieldType == int.class || fieldType == Integer.class) {

                    if (fieldName.equals("status") || fieldName.equals("type")) {
                        // 名称是 status 或是 type，数据库估计是 byte 类型，取模 128
                        field.set(obj, ((int) num) % 128);
                    } else {
                        // 若是整型，直接赋值
                        field.set(obj, num);
                    }
                } else if (fieldType == long.class || fieldType == Long.class) {
                    field.set(obj, (long) num);
                } else if (fieldType == double.class || fieldType == Double.class) {
                    field.set(obj, (double) num);
                } else if (fieldType == float.class || fieldType == Float.class) {
                    field.set(obj, (float) num);
                } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                    field.set(obj, num % 2 == 0);
                } else if (field.getType() == Date.class) {
                    field.set(obj, new Date());
                }
                // 其他类型不设置，留空
            }
            // 返回生成的对象
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}