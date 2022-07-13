package com.lab.business.ro;

import com.lab.business.message.LabMessage;
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
public class LabCreateRO {

    @NotNull(message = LabMessage.NAME_NOT_EMPTY)
    private String name;

    private String descInfo;

    @NotNull(groups = Service.class)
    private Long createBaseUserId;

}
