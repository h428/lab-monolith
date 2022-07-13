package com.lab.common.constant;

/**
 * Regexp : Regular Expression，正则表达式
 * @author hao
 */
public interface CommonRegexpPattern {

    /**
     * 用户名只能包含中英文、数字、下划线、减号，长度必须在 2 - 16 之间
     */
    String USERNAME = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\-_]{2,16}$";

    /**
     * 密码：密码必须同时包含字母和数字，长度至少为 8，若有特殊字符则只允许 @#$()+-/*%^&,.?! 这几个特殊字符
     */
    String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$()+\\-*/%^&,.?!]{8,}$";

    /**
     * 不能全为空白符
     */
    String NOT_BLANK = "^(?!^\\s+$)[\\s\\S]+$";

    /**
     * 邮箱
     */
    String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
}
