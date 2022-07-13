package com.lab.business.message;

public interface LabJoinLinkMessage {

    String NOT_EXIST = "实验室授权链接不存在";

    String ID = "id";
    String ID_NOT_NULL = ID + " 不能为空";

    String LAB_ID = "所属实验室";
    String LAB_ID_NOT_NULL = LAB_ID + "不能为空";

    String LAB_ROLE_ID = "实验室角色";
    String LAB_ROLE_ID_NOT_NULL = LAB_ROLE_ID + "不能为空";
}
