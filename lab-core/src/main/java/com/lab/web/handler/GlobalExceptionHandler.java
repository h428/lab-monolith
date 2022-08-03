package com.lab.web.handler;

import com.lab.common.exception.BaseException;
import com.lab.common.bean.ResBean;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 统一处理继承于 BaseWebException 的全局异常（包含了状态码）
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResBean<?> handleBaseWebException(HttpServletResponse response, BaseException ex) {
        return ResBean.build(ex.getStatus(), ex.getMessage(), null);
    }

    // RequestBody 类请求校验失败产生的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResBean<?> handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return ResBean.badRequest_400(fieldError.getDefaultMessage());
    }

    // 表单类请求校验失败产生的异常
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResBean<?> handleBindException(BindException ex) {
        //校验 除了 requestBody 注解方式的参数校验 对应的 bindingResult 为 BeanPropertyBindingResult
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return ResBean.badRequest_400(fieldError.getDefaultMessage());
    }

    // 路径参数绑定失败产生的异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResBean<?> handleBindException(ConstraintViolationException ex) {
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return ResBean.badRequest_400(constraintViolation.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResBean<?> handleBindException(Exception ex) {
        //校验 除了 requestBody 注解方式的参数校验 对应的 bindingResult 为 BeanPropertyBindingResult
        log.debug(ex.getMessage());
        return ResBean.internal_server_error_500("处理发生异常");
    }
}
