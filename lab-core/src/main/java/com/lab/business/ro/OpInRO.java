package com.lab.business.ro;

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
public class OpInRO {

    @NotNull(message = "鉴权需要：实验室 id 不能为空")
    private Long labId;

    @NotNull(message = "入库架子 id 不能为空")
    private Long labShelfId;

    @NotNull(message = "入库物品 id 不能为空")
    private Long labItemId;

    @NotNull(message = "入库数量不能为空")
    private Integer num;

    @NotNull(message = "入库价格不能为空")
    private Integer price;

    private String opInfo;

    @NotNull(groups = Group.Service.class, message = "操作实验室用户 id 不能为空")
    private Long opLabUserId;


}
