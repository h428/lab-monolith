package com.lab.business.ro;

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
public class OpConfirmRO {

    @NotNull(message = "鉴权需要：实验室 id 不能为空")
    private Long labId;

    @NotNull(message = "申请单 id 不能为空")
    private Long labApplyId;

    @NotNull(message = "审批意见不能为空")
    private Boolean accept;


    @NotNull(groups = Service.class)
    private Long confirmLabUserId;

}
