package com.lab.web.perm.processor;

import cn.hutool.core.util.StrUtil;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.common.exception.NoPermissionException;
import com.lab.web.perm.anno.LabPerm;
import java.lang.reflect.Method;

public class LabPermProcessor extends CommonProcessor<LabPerm> {

    @Override
    protected void check(Method method) {

        // 取出方法上的注解值
        LabPerm annotation = method.getAnnotation(super.annotationClass);
        String perm = annotation.value();

        // 若没有配置权限信息，直接跳过
        if (StrUtil.isBlank(perm)) {
            return;
        }

        // 若不具备指定的权限直接抛出异常让全局异常处理器处理
        if (!LabPermContext.hasPerm(perm)) {
            throw new NoPermissionException();
        }
    }

}
