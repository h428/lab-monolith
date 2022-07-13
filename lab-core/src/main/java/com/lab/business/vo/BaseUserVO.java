package com.lab.business.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class BaseUserVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    private String name; // 姓名

    private String phone; // 电话

    private String wechatNo; // 微信号

    private String avatar; // 头像

    private Long createTime; // 注册时间

    private Long lastLoginTime; // 上次登录时间

    private Integer status; // 状态

    private Boolean allowAdd;

}
