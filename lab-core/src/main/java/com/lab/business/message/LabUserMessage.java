package com.lab.business.message;

public interface LabUserMessage {

    String NOT_EXIST = "实验室成员不存在";

    String ID = "id";
    String ID_NOT_NULL = ID + " 不能为空";

    String LAB_ID = "实验室成员所属实验室 id";
    String LAB_ID_NOT_NULL = LAB_ID + " 不能为空";

    String BASE_USER_ID = "实验室成员对应的基础用户 id";
    String BASE_USER_ID_NOT_NULL = BASE_USER_ID + " 不能为空";

    String NAME = "实验室成员名称";
    String NAME_NOT_EMPTY = NAME + "不能为空";

    String ROLE_ID = "实验室角色 id";
    String ROLE_ID_NOT_NULL = ROLE_ID + " 不能为空";

    String LAB_PERMISSIONS = "实验室成员额外权限";

    String CREATE_LAB_USER_ID = "创建人的实验室成员 id";
    String CREATE_LAB_USER_ID_NOT_NULL = CREATE_LAB_USER_ID + " 不能为空";

    String BELONG_USER_CANNOT_LEAVE = "实验室拥有者无法退出实验室";
}
