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
public class LabInVO {

    private Long id;

    private Long labItemId;

    private Integer num;

    private Integer price;

    private String opInfo;

    private Long opTime;

    private Long opLabShelfId;

    private Long opLabUserId;

    private Long labId;

    private Boolean deleted;

    private Long deleteTime;


}
