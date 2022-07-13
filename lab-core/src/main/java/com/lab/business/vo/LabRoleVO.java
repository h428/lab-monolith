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
public class LabRoleVO {

    private Long id;

    private Long labId;

    private String name;

    private String descInfo;

    private String labPerms;

    private Long createLabUserId;

    private Long createTime;

    private Long updateLabUserId;

    private Long updateTime;

    public void addOwn() {
        this.labPerms = this.labPerms + ",own";
    }

    public void addAddedIn() {
        this.labPerms = this.labPerms + ",added-in";
    }



}
