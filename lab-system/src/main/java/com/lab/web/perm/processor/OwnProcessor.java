package com.lab.web.perm.processor;

import com.lab.business.threadlocal.LabPermContext;
import com.lab.common.exception.NoPermissionException;
import com.lab.web.perm.anno.Own;
import java.lang.reflect.Method;

public class OwnProcessor extends CommonProcessor<Own> {

    @Override
    protected void check(Method method) {
        // 若不具备指定的权限直接抛出异常让全局异常处理器处理
        if (!LabPermContext.own()) {
            throw new NoPermissionException();
        }
    }

}
