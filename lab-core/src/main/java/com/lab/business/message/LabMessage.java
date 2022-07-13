package com.lab.business.message;

public interface LabMessage {

    String ID = "实验室 id";
    String ID_NOT_NULL = ID + " 不能为空";

    String NAME = "名称";
    String NAME_NOT_EMPTY = NAME + "不能为空";

    String LAB_ID_DATA_ID_INCONSISTENCY = "实验室 id 和数据 id 不一致";

    String NOT_EXIST = "实验室不存在";

}
