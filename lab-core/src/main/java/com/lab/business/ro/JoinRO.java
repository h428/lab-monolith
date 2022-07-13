package com.lab.business.ro;

import com.lab.business.message.LabUserMessage;
import com.lab.common.constant.Group;
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
public class JoinRO {

    @NotNull(message = "加入链接不能为空")
    private String link;

    @NotNull(message = LabUserMessage.NAME_NOT_EMPTY)
    private String labUserName;

    @NotNull(groups = Group.Service.class, message = "baseUserId 不能为 null")
    private Long baseUserId;

}
