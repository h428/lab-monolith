package com.lab.business.ro;

import com.lab.business.message.LabRoleMessage;
import com.lab.common.constant.Group.Service;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class LabRoleCreateRO {

    @NotEmpty(message = LabRoleMessage.NAME_NOT_EMPTY)
    private String name;

    private String descInfo;

    @NotNull(message = LabRoleMessage.LAB_ID)
    private String labId;

    @Length(max = 1024, message = LabRoleMessage.LAB_PERMS)
    private String labPerms;

    @NotNull(groups = Service.class)
    private Long opLabUserId;

}
