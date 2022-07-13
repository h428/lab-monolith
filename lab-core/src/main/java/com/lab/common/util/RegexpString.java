package com.lab.common.util;

public class RegexpString {

    private RegexpString() {

    }

    // xls, xlsx 文件
    public static final String IMG = "^.+\\.(?i)(jpg|jpeg|png)$";

    // jpg, jpeg, png 文件
    public static final String XLS = "^.+\\.(?i)(xls|xlsx)$";
    public static final String XLS_2003 = "^.+\\.(?i)(xls)$";

    // email
    public static final String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    // 用户名只能由字母、数字、下划线构成，长度至少为 3，不得超过 16
    public static final String USER_NAME = "^[a-zA-Z0-9_]{3,16}$";

    // 密码必须同时包含字母和数字，长度至少为8，若有特殊字符则只允许 @#$()+-*/%^&,.?! 这几个特殊字符
    public static final String USER_PASS = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$()+\\-*/%^&,.?!]{8,}$";

    public static void main(String[] args) {
        System.out.println("aaa.jpEG".matches(IMG));
        System.out.println("aaa.JpG".matches(IMG));
        System.out.println("aaa".matches(IMG));
        System.out.println("aaa.jud".matches(IMG));

        System.out.println("aaa.xls".matches(XLS));
        System.out.println("aaa.xlsx".matches(XLS));

    }

}
