package com.lab.business.ro;

import com.lab.common.constant.Group.Service;
import com.lab.entity.LabInventory.Tid;
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
public class OpMoveRO {

    @NotNull(message = "鉴权需要：实验室 id 不能为空")
    private Long labId;

    @NotNull(message = "源架子 id 不能为空")
    private Long fromLabShelfId;

    @NotNull(message = "目标架子 id 不能为空")
    private Long toLabShelfId;

    @NotNull(message = "labInId 不能为空")
    private Long labInId;

    @NotNull(message = "capacityId 不能为空")
    private Long capacityId;

    @NotNull(message = "移动数量不能为空")
    private Integer num;

    private String opInfo;

    @NotNull(groups = Service.class)
    private Long opLabUserId;

    public Tid fromTid() {
        return Tid.builder()
            .labShelfId(this.fromLabShelfId)
            .labInId(labInId)
            .capacityId(capacityId)
            .build();
    }

    public Tid toTid() {
        return Tid.builder()
            .labShelfId(this.toLabShelfId)
            .labInId(labInId)
            .capacityId(capacityId)
            .build();
    }

}
