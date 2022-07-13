package com.lab.business.enums.op;

/**
 * 申请操作的结果
 */
public enum OpApplyResultEnum {
    CONFIRMED, // 申请且自动审批通过
    TO_BE_CONFIRM, // 申请发起成功，但待审批
    ;
}
