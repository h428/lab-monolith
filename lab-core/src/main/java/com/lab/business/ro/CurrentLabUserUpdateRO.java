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
public class CurrentLabUserUpdateRO {

    @ApiModelProperty(LabUserMessage.NAME)
    @NotEmpty(message = LabUserMessage.NAME_NOT_EMPTY)
    private String name;

    @NotNull(message = LabUserMessage.LAB_ID_NOT_NULL)
    private Long labId;

}
