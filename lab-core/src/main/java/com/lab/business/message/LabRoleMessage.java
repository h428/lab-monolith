package com.lab.business.message;

public interface LabRoleMessage {

    String NOT_EXIST = "实验室角色不存在";

    String ID = "id";
    String ID_NOT_NULL = ID + " 不能为空";

    String NAME = "角色名称";
    String NAME_NOT_EMPTY = NAME + "不能为空";

    String LAB_PERMS = "权限字符串";
    String LAB_PERMS_LENGTH_MESSAGE = LAB_PERMS + "长度不得超过 1024 个字符";

    String LAB_ID = "所属实验室";
    String LAB_ID_NOT_NULL = LAB_ID + "不能为空";

}
