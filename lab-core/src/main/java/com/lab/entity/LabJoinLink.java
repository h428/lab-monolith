package com.lab.entity;

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
public class LabJoinLink {

    private Long id;

    private String link;

    private Long labId;

    private Long labRoleId;

    private Long createTime;

    private Long createLabUserId;

}
