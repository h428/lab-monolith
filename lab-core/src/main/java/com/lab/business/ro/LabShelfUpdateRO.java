package com.lab.business.ro;

import com.lab.business.message.LabShelfMessage;
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
public class LabShelfUpdateRO {

    @NotNull(message = LabShelfMessage.ID_NOT_NULL)
    private Long id;

    private String code;

    private String name;

    private String pos;

    @NotNull(message = LabShelfMessage.LAB_ID_NOT_NULL)
    private Long labId;

    @NotNull(groups = Service.class)
    private Long opLabUserId;

}
