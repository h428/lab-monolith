package com.lab.business.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class LabVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    private Integer type;

    private String descInfo;

    private Long belongBaseUserId;

    private Long createBaseUserId;

    private Long createTime;

    private Long updateBaseUserId;

    private Long updateTime;

    private Long handOverUserId;

    private Integer status;

}
