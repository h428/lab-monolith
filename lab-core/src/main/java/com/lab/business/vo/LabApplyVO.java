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
public class LabApplyVO {

    private Long id;

    private Long labInId;

    private Long capacityId;

    private Integer num;

    private String opInfo;

    private Long opTime;

    private Long confirmTime;

    private Long fromLabShelfId;

    private Long toLabShelfId;

    private Long fromLabUserId;

    private Long toLabUserId;

    private Long confirmLabUserId;

    private Boolean move;

    private Long labItemId;

    private Long labId;

    private Integer status;

}
