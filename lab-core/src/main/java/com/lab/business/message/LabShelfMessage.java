package com.lab.business.message;

public interface LabShelfMessage {

    String NOT_EXIST = "实验室架子不存在";

    String ID = "id";
    String ID_NOT_NULL = ID + " 不能为空";

    String NAME = "实验室架子名称";
    String NAME_NOT_EMPTY = NAME + "不能为空";

    String TYPE = "架子类型";
    String TYPE_NOT_NULL = TYPE + " 不能为空";

    String LAB_ID = "实验室物品所属实验室 id";
    String LAB_ID_NOT_NULL = LAB_ID + " 不能为空";

}
