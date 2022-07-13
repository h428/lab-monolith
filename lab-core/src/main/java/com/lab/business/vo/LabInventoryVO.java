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
public class LabInventoryVO {

    private Long id;

    private Long labShelfId;

    private Long labInId;

    private Long capacityId;

    private Integer num;

    private Integer capacity;

    private Long labId;

    private Long labItemId;

    private Long labUserId;

    private Integer version;

}
