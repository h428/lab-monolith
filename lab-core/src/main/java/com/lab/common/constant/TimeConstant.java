package com.lab.common.constant;

/**
 * 缓存时间常量，结果单位为描述
 *
 * @author hao
 */
public interface TimeConstant {

    /**
     * 30 分钟的秒数
     */
    Long SECONDS_OF_HALF_HOUR = 30L * 60L;

    /**
     * 60 分钟的秒数
     */
    Long SECONDS_ONE_HOUR = 60L * 60L;

    /**
     * 3 小时的秒数
     */
    Long SECONDS_OF_THREE_HOUR = 3L * 60L * 60L;
}
