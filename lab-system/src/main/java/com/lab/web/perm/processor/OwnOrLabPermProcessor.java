package com.lab.web.perm.processor;

import cn.hutool.core.util.StrUtil;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.common.exception.NoPermissionException;
import com.lab.web.perm.anno.OwnOrLabPerm;
import java.lang.reflect.Method;

public class OwnOrLabPermProcessor extends CommonProcessor<OwnOrLabPerm> {

    @Override
    protected void check(Method method) {

        // 取出方法上的注解值
        OwnOrLabPerm annotation = method.getAnnotation(OwnOrLabPerm.class);
        String perm = annotation.value();

        // 若没有配置权限信息，直接跳过
        if (StrUtil.isBlank(perm)) {
            return;
        }

        // 若不具备指定的权限直接抛出异常让全局异常处理器处理
        if (!LabPermContext.own() && !LabPermContext.hasPerm(perm)) {
            throw new NoPermissionException();
        }
    }

}
