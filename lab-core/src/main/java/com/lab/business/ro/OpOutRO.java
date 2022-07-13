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
public class OpOutRO {

    @NotNull(message = "鉴权需要：实验室 id 不能为空")
    private Long labId;

    @NotNull(message = "库存 id 不能为空")
    private Long id;

    @NotNull(message = "消耗数量不能为空")
    private Integer num;

    private String opInfo;

    @NotNull(groups = Service.class)
    private Long opLabUserId;


}
