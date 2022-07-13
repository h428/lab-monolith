package com.lab.common.util;

import com.lab.common.exception.BaseException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 校验工具类，依赖 Hibernate Validator，可基于 java.validation 注解对 pojo 做校验
 */
public class ValidationUtil {

    // 为了通用性，这里自己初始化校验器而不进行注入，这样子类可以不依赖 Spring 环境，对于 boot-ssm 直接注入也是可以的
    public static final Validator validator;

    static {
        // 初始化方式一：直接使用 Spring 容器中的 Validator，依赖 Spring 环境，有 Spring 环境时可采用该种方式
        // validator = SpringContextUtil.getBean(Validator.class);

        // 初始化方式二：自行初始化 validator，不依赖 spring 环境，但需要确保引入 hibernate-validator 包
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    private static <T> void checkResult(Set<ConstraintViolation<T>> constraintViolationSet) {
        if (constraintViolationSet != null && !constraintViolationSet.isEmpty()) {
            // 短路：抛出全局通用异常
            for (ConstraintViolation<T> constraintViolation : constraintViolationSet) {
                throw new BaseException(400, constraintViolation.getMessage());
            }
        }
    }

    public static <T> void validate(T entity, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(entity, groups);
        checkResult(constraintViolationSet);

    }

    public static <T> void validateMethodParameters(T methodInstance, Method method,
        Object[] args, Class<?>... groups) {

        Set<ConstraintViolation<T>> constraintViolationSet =
            validator.forExecutables().validateParameters(methodInstance, method, args, groups);
        checkResult(constraintViolationSet);
    }

}
