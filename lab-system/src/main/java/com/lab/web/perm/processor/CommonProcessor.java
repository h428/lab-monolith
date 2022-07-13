package com.lab.web.perm.processor;

import com.lab.business.threadlocal.LabIdThreadLocal;
import com.lab.common.exception.NoPermissionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class CommonProcessor<T extends Annotation> {

    // 参数化类型参数：即子类的注解类型
    protected Class<T> annotationClass;

    @SuppressWarnings("unchecked")
    public CommonProcessor() {
        // 获取父类类型并转化为参数化类型 ParameterizedType 以获取泛型信息
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        // 使用参数化类型获取泛型的实际信息
        this.annotationClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    public void ensureLabIdNotNull() {

        Long labId = LabIdThreadLocal.get();

        if (labId == null) {
            throw new NoPermissionException("未设置实验室，权限不足");
        }

    }

    protected abstract void check(Method method);

    public final void process(Method method) {

        // 若指定方法没有添加注解，直接返回不做后续校验
        if (!method.isAnnotationPresent(annotationClass)) {
            return;
        }

        // 由于是基于 labId 做权限校验，校验权限之前，确保 labId 存在
        // 所有打上权限校验的注解的 Http 请求，其 pathParam, requestParam, body 至少有一个指明了 labId
        // 这样才能解析出 labId 并存储在 LabIdThreadLocal 中
        ensureLabIdNotNull();

        // 执行子类的校验
        check(method);
    }



}
