package com.lab.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

public class PojoUtil {

    public static void copyPropertiesNotNull(Object source, Object target, String... ignoreProperties) {
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        copyOptions.setIgnoreProperties(ignoreProperties);
        BeanUtil.copyProperties(source, target, copyOptions);
    }


}
