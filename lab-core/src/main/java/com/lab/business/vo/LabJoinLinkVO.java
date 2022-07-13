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
public class LabJoinLinkVO {

    private Long id;

    private String link;

    private Long labId;

    private Long labRoleId;

    private Long createTime;

    private Long createLabUserId;

    // lab
    private String labName;

    // labRole
    private String labRoleName;

    public static final LabJoinLinkVO EMPTY = LabJoinLinkVO.builder().id(-1L).build();

}
