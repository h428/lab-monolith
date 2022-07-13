package com.lab.entity;

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
public class BaseUser {

    private Long id;

    private String email;

    private String password;

    private String salt;

    private String username;

    private String name;

    private String phone;

    private String wechatNo;

    private String avatar;

    private Long createTime;

    private Long updateTime;

    private Long lastLoginTime;

    private Integer status;

    private Boolean allowAdd;

    private Boolean allowAuth;

    private Boolean allowMessage;

    private Boolean deleted;

    private Long deleteTime;

}
