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
public class LabUserVO {

    private Long id;

    private Long labId;

    private Long baseUserId;

    private String name;

    private Long labRoleId;

    private Long createLabUserId;

    private Long createTime;

    private Long updateLabUserId;

    private Long updateTime;

    // 补充 LabRole 信息
    private LabRoleVO labRole;
}
