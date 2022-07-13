package com.lab.business.message;

import com.lab.common.constant.CommonRegexpPattern;

public interface BaseUserMessage {

    String EMAIL_NOT_BLANK_MESSAGE = "邮箱不能为空";
    String EMAIL_MESSAGE = "邮箱格式不正确";

    /**
     * 密码
     */
    String PASSWORD_NOT_BLANK_MESSAGE = "密码不能为空";
    String PASSWORD_PATTERN = CommonRegexpPattern.PASSWORD;
    String PASSWORD_PATTERN_MESSAGE = "密码必须同时包含字母和数字，长度至少为8，若有特殊字符则只允许 @#$()+-*/%^&,.?! 这几个特殊字符";


    String USERNAME_NOT_BLANK_MESSAGE = "用户名不能为空";
    String USERNAME_PATTERN = CommonRegexpPattern.USERNAME;
    String USERNAME_PATTERN_MESSAGE = "户名只能包含中英文、数字、下划线、减号，长度必须在 2 - 16 之间";

    String CAPTCHA_NOT_BLANK = "验证码不能为空";
    String CAPTCHA_PATTERN_REGEXP = "^[0-9]{6}$";
    String CAPTCHA_PATTERN_MESSAGE = "验证码格式不正确";
}
