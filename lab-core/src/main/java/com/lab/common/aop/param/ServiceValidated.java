package com.lab.common.aop.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

/**
 * 用于开启 Service 层方法入参校验的注解，配合 com.lab.common.aop.param.ServiceParamsValidator 完成参数校验；
 * 注意，aop 使用的是 @annotation(com.lab.common.aop.param.ServiceValidated)，经过测试，该注解必须打在实现类
 * 的方法上，如果打在接口的方法上无效
 */
@Target(ElementType.METHOD)
@Inherited
public @interface ServiceValidated {

}
