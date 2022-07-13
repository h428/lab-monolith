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
public class LabItem {

    private Long id;

    private String name;

    private String engName;

    private String descInfo;

    private Integer price;

    private String unit;

    private String itemNo;

    private String cas;

    private Boolean floated;

    private Long updateLabUserId;

    private Long updateTime;

    private Long labId;

    private Integer capacity;

    private String capacityUnit;

    private Long createLabUserId;

    private Long createTime;

    private Boolean deleted;

    private Long deleteTime;

}
