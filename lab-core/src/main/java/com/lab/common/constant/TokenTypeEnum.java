package com.lab.common.constant;

public enum TokenTypeEnum {
    BASE_USER("BASE_USER_TOKEN:"),
    ADMIN("BASE_USER_TOKEN:"),
    ;

    TokenTypeEnum(String TOKEN_PREFIX) {
        this.TOKEN_PREFIX = TOKEN_PREFIX;
    }

    public final String TOKEN_PREFIX;

}
