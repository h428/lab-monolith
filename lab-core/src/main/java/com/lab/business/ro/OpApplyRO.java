package com.lab.business.ro;

import com.lab.business.message.LabInventoryMessage;
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
public class OpApplyRO {

    @NotNull(message = "鉴权需要：实验室 id 不能为空")
    private Long labId;

    @NotNull(message = LabInventoryMessage.ID_NOT_NULL)
    private Long id;

    @NotNull(message = "申请数量不能为空")
    private Integer num;

    private String opInfo;

    @NotNull(message = "目标架子 id 不能为空 ")
    private Long toLabShelfId;

}
