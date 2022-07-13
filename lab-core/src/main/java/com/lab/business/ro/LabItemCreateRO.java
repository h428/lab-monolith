package com.lab.business.ro;

import com.lab.business.message.LabItemMessage;
import com.lab.common.constant.Group.Service;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class LabItemCreateRO {

    @NotEmpty(message = LabItemMessage.NAME_NOT_EMPTY)
    private String name;

    private String engName;

    private String descInfo;

    @NotNull(message = LabItemMessage.PRICE_NOT_NULL)
    private Integer price;

    private String unit;

    private String itemNo;

    private String cas;

    @NotNull(message = LabItemMessage.FLOATED_NOT_NULL)
    private Boolean floated;

    @NotNull(message = LabItemMessage.LAB_ID_NOT_NULL)
    private Long labId;

    @NotNull(message = LabItemMessage.CAPACITY_NOT_NULL)
    private Integer capacity;

    private String capacityUnit;

    @NotNull(groups = Service.class)
    private Long opLabUserId;

}
