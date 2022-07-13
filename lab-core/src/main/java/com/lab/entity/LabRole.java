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
public class LabRole {

    private Long id;

    private Long labId;

    private String name;

    private String descInfo;

    private String labPerms;

    private Long createLabUserId;

    private Long createTime;

    private Long updateLabUserId;

    private Long updateTime;

}
