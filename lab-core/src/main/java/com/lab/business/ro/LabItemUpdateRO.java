package com.lab.business.ro;

import com.lab.business.message.LabItemMessage;
import com.lab.common.constant.Group.Service;
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
public class LabItemUpdateRO {

    @NotNull(message = LabItemMessage.ID_NOT_NULL)
    private Long id;

    private String name;

    private String engName;

    private String descInfo;

    private Integer price;

    private String unit;

    private String itemNo;

    private String cas;

    private Boolean floated;

    @NotNull(message = LabItemMessage.LAB_ID_NOT_NULL)
    private Long labId;

    private Integer capacity;

    private String capacityUnit;

    @NotNull(groups = Service.class)
    private Long opLabUserId;

}
