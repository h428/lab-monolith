package com.lab.business.message;

public interface BaseUserMessage {

    String EMAIL_NOT_BLANK = "邮箱不能为空";
    String EMAIL_INVALID = "邮箱格式不正确";

    String CONFIRM_PASSWORD_NOT_BLANK = "确认密码不能为空";

    String OLD_PASSWORD_NOT_BLANK = "旧密码不能为空";

    /**
     * 密码
     */
    String PASSWORD_NOT_BLANK = "密码不能为空";
    String PASSWORD_INVALID = "密码必须同时包含字母和数字，长度至少为8，若有特殊字符则只允许 @#$()+-*/%^&,.?! 这几个特殊字符";


    String USERNAME_NOT_BLANK_MESSAGE = "用户名不能为空";

    String USERNAME_INVALID = "户名只能包含中英文、数字、下划线、减号，长度必须在 2 - 16 之间";

    String CAPTCHA_NOT_BLANK = "验证码不能为空";

    String CAPTCHA_INVALID = "验证码格式不正确";


    String SEND_EMAIL_TOO_FREQUENT = "发送邮件过于频繁，请稍后重试";

    String EMAIL_USED = "邮箱已被注册";

    String EMAIL_UNUSED = "邮箱未注册";

    String CAPTCHA_ERROR = "验证码错误";

    String USERNAME_USED = "用户名已被占用";
}
