package com.lab.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

// 基础全局异常类，继承该类的异常都会被 Spring 的全局异常处理器捕捉，并转化为 ResBean 对应的 json 形式
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class BaseException extends RuntimeException {

    private int status;

    private String message;

}
