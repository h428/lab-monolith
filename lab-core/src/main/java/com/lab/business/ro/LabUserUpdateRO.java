package com.lab.business.ro;

import com.lab.business.message.LabUserMessage;
import com.lab.common.constant.Group;
import io.swagger.annotations.ApiModelProperty;
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
public class LabUserUpdateRO {

    @ApiModelProperty(LabUserMessage.ID)
    @NotNull(message = LabUserMessage.ID_NOT_NULL)
    private Long id;

    @ApiModelProperty(LabUserMessage.NAME)
    private String name;

    @ApiModelProperty(LabUserMessage.ROLE_ID)
    private Long labRoleId;

    @NotNull(groups = Group.Service.class)
    private Long opLabUserId;

}
