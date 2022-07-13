package com.lab.business.ro;

import com.lab.business.message.LabUserMessage;
import io.swagger.annotations.ApiModelProperty;
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
public class LabUserCreateRO {

    @ApiModelProperty(LabUserMessage.LAB_ID)
    @NotNull(message = LabUserMessage.LAB_ID_NOT_NULL)
    private Long labId;

    @ApiModelProperty(LabUserMessage.BASE_USER_ID)
    @NotNull(message = LabUserMessage.BASE_USER_ID_NOT_NULL)
    private Long baseUserId;

    @ApiModelProperty(LabUserMessage.NAME)
    @NotEmpty(message = LabUserMessage.NAME_NOT_EMPTY)
    private String name;

    @ApiModelProperty(LabUserMessage.ROLE_ID)
    @NotEmpty(message = LabUserMessage.ROLE_ID_NOT_NULL)
    private Long labRoleId;

    @ApiModelProperty(LabUserMessage.CREATE_LAB_USER_ID)
    @NotEmpty(message = LabUserMessage.CREATE_LAB_USER_ID_NOT_NULL)
    private Long createLabUserId;

}
