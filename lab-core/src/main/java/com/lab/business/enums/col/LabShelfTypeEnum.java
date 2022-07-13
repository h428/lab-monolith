package com.lab.business.enums.col;

/**
 * 架子类型
 */
public enum LabShelfTypeEnum {
    PRIVATE, // 私人架子，数组库值为 0
    PUBLIC, // 公共架子，数据库值为 1，此时对应的 hub_user_id 列为 0
    STORAGE, // 贮藏架子，数据库值为 2，此时对应的 hub_user_id 列为 0
    LOCK // 带锁的架子，数据库值为 3，此时对应的 hub_user_id 列为 0，保留类型
    ;

    public static final int MIN = 0;
    public static final int MAX = 2; // 暂时是 2，因为 LOCK 类型是保留的


}
