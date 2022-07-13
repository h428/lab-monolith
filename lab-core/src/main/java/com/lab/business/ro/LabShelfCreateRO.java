package com.lab.business.ro;

import com.lab.business.message.LabShelfMessage;
import com.lab.business.enums.col.LabShelfTypeEnum;
import com.lab.common.constant.Group.Service;
import com.lab.common.exception.ParamErrorException;
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
public class LabShelfCreateRO {

    private String code;

    @NotEmpty(message = LabShelfMessage.NAME_NOT_EMPTY)
    private String name;

    private String pos;

    @NotNull(message = LabShelfMessage.TYPE_NOT_NULL)
    private Integer type;

    @NotNull(message = LabShelfMessage.LAB_ID_NOT_NULL)
    private Long labId;

    private Long belongLabUserId;

    @NotNull(groups = Service.class)
    private Long opLabUserId;

    /**
     * 额外的参数校验
     */
    public void validate() {

        // 若是架子类型为“私人架子”，则还必须设置对应的实验室用户 id
        if (type == LabShelfTypeEnum.PRIVATE.ordinal()
            && belongLabUserId == null) {
            throw new ParamErrorException("私人架子必须设置所属用户 id");
        }

    }
}
