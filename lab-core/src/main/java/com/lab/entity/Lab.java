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
public class Lab {

    private Long id;

    private String name;

    private String descInfo;

    private Long belongBaseUserId;

    private Long createBaseUserId;

    private Long createTime;

    private Long updateBaseUserId;

    private Long updateTime;

    private Integer status;

    private Boolean deleted;

    private Long deleteTime;

}
