package com.lab.business.ro;

import com.lab.business.message.LabJoinLinkMessage;
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
public class LabJoinLinkCreateRO {

    @NotNull(message = LabJoinLinkMessage.LAB_ROLE_ID_NOT_NULL)
    private Long labRoleId;

    @NotNull(message = LabJoinLinkMessage.LAB_ID_NOT_NULL)
    private Long labId;

    private Long opLabUserId;



}
