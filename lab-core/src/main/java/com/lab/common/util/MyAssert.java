package com.lab.common.util;

import com.lab.common.exception.BusinessException;
import java.util.Objects;

/**
 * 自定义断言工具类，失败会抛出 BusinessException 异常供全局捕获
 */
public class MyAssert {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    public static void equals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw new BusinessException(message);
        }
    }

    public static void notEquals(Object a, Object b, String message) {
        if (Objects.equals(a, b)) {
            throw new BusinessException(message);
        }
    }

    public static void notNull(Object o, String message) {
        if (o == null) {
            throw new BusinessException(message);
        }
    }


}
