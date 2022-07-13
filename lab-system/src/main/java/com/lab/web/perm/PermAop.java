package com.lab.web.perm;

import com.lab.business.threadlocal.BaseUserIdThreadLocal;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.web.perm.processor.AddedInProcessor;
import com.lab.web.perm.processor.CommonProcessor;
import com.lab.web.perm.processor.LabPermProcessor;
import com.lab.web.perm.processor.OwnOrAddedInProcessor;
import com.lab.web.perm.processor.OwnOrLabPermProcessor;
import com.lab.web.perm.processor.OwnProcessor;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Aspect
@Component
public class PermAop {

    /**
     * 定义切面：com.lab.web.controller 包下的所有类的所有方法
     */
    @Pointcut("execution(public * com.lab.web.controller.*.*(..))")
    public void aspect() {

    }

    // 组装需要处理的权限注解处理链
    // 有点类似职责链模式，但由于校验校验无需嵌套调用，只需用 list 简单组合即可
    static final CommonProcessor<?>[] processors = {
        new AddedInProcessor(),
        new OwnProcessor(),
        new OwnOrAddedInProcessor(),
        new LabPermProcessor(),
        new OwnOrLabPermProcessor()
    };

    /**
     * 权限环绕通知
     */
    @ResponseBody
    @Around("aspect()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取 aop 拦截的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();

        // 统一处理前置，baseUserId 的上下文必须有值，才继续进行后续的 AOP 权限判定
        Long baseUserId = BaseUserIdThreadLocal.get();

        if (baseUserId == null) {
            return joinPoint.proceed();
        }


        // 初始化权限校验上下文
        LabPermContext.init();

        // CommonProcessor 提供模板方法，要求对所有子类处理器都 ensureLabIdNotNull
        // 即如果打了权限注解，但请求为解析到 labId 会抛出异常
        for (CommonProcessor<?> processor : processors) {
            processor.process(targetMethod);
        }


        // 所有权限校验通过，放行，并拿到 Controller 方法的返回结果
        Object res = joinPoint.proceed();

        // 移除上下文
        LabPermContext.remove();

        return res;
    }

}
