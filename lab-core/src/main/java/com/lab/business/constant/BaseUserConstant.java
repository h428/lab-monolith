package com.lab.business.constant;

import com.lab.common.constant.CommonRegexpPattern;

public interface BaseUserConstant {


    String PASSWORD_PATTERN = CommonRegexpPattern.PASSWORD;
    String USERNAME_PATTERN = CommonRegexpPattern.USERNAME;
    String CAPTCHA_PATTERN_REGEXP = "^[0-9]{6}$";

}
