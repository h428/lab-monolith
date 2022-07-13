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
public class LabShelfVO {

    private Long id;
    private String code;
    private String name;
    private String pos;
    private Integer type;

    private Long labId;

    private Long belongLabUserId;

    private Long createLabUserId;

    private Long createTime;

    private Long updateLabUserId;

    private Long updateTime;

    private Boolean deleted;

    private Long deleteTime;


}
