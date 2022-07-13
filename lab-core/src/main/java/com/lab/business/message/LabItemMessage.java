package com.lab.business.message;

public interface LabItemMessage {

    String ID = "id";
    String ID_NOT_NULL = ID + " 不能为空";

    String NAME = "实验室物品名称";
    String NAME_NOT_EMPTY = NAME + "不能为空";

    String ENG_NAME = "英文名称";
    String ENG_NAME_NOT_EMPTY = ENG_NAME + "不能为空";

    String PRICE = "价格";
    String PRICE_NOT_NULL = PRICE + "不能为空";

    String FLOATED = "是否浮点数";
    String FLOATED_NOT_NULL = FLOATED + "不能为空";

    String LAB_ID = "实验室物品所属实验室 id";
    String LAB_ID_NOT_NULL = LAB_ID + " 不能为空";

    String CAPACITY = "容量";
    String CAPACITY_NOT_NULL = CAPACITY + "不能为空";

    String NOT_EXIST = "实验室物品不存在";
}
