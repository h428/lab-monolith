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
public class LabOutVO {

    private Long id;

    private Long labInId;

    private Long capacityId;

    private Integer num;

    private String opInfo;

    private Long opTime;

    private Long opLabShelfId;

    private Long opLabUserId;

    private Long labItemId;

    private Long labId;

}
