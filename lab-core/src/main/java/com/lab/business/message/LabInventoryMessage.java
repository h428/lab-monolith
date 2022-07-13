package com.lab.business.message;

public interface LabInventoryMessage {

    String ID = "库存 id";
    String ID_NOT_NULL = ID + "不能为空";

    String LAB_ID = "所属实验室 id ";
    String LAB_ID_NOT_NULL = LAB_ID + "不能为空";

    String NOT_EXIST = "实验室库存不存在";

    String OP_NUM_EXCEED_LEFT = "库存不足";

    String OP_NUM_EXCEED_VOLUME = "规格品单次消耗不得超过规格量";

}
