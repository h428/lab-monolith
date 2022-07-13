package com.lab.common.aop.param;

import com.lab.common.constant.Group;
import com.lab.common.util.ValidationUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.aopalliance.aop.Advice;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * ServiceValidated 注解的 aop 逻辑
 */
@Aspect
@Component
public class ServiceValidatedAop {


    /**
     * 经测试，其中 @ServiceValidated 注解必须打在实现类上才会生效，否则不会进入本 aop 拦截；
     *
     * 此外，校验器对方法重写做了约束，要求子类方法的参数列表不能覆盖父类的定义，否则会抛出异常；
     * 因此，对于需要校验的方法，如果有 @Valid, @NotEmpty 之类的注解，子类中打上后，接口中也必须同步打上；
     * 并且经测试，可以只在接口中打上 @Valid 之类的注解而子类省略；
     *
     * 因此最简配置是在实现类的方法打上 @ServiceValidated 注解，在接口的方法定义中打上 @Valid 之类的注解；
     * 但这样很奇怪，因此建议在接口方法定义处打上完整的注解：包括方法上的 @ServiceValidated 和方法参数列表
     * 上的 @Valid 之类的注解，这样可以提高接口的可读性；
     *
     * 对于实现类，必须要打 @ServiceValidated 注解才能进入 aop，至于方法参数上的 @Valid 之类的注解如果要
     * 打，则必须和接口中的一致（不可修改定义），也可以省略；但同一个项目内最好保持风格一致，本项目采取的是省略的做法；
     *
     * @param joinPoint
     */
    @Before("@annotation(com.lab.common.aop.param.ServiceValidated)")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 调用 ValidationUtil 完成参数校验，注意默认 group = Group.Service.class
        ValidationUtil.validateMethodParameters(
             joinPoint.getThis(), signature.getMethod(), args, Group.Service.class);

    }
}
