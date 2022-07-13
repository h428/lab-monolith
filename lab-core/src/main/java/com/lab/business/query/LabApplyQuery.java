package com.lab.business.query;

import com.lab.business.message.LabMessage;
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
public class LabApplyQuery {

    @NotNull(message = LabMessage.ID_NOT_NULL)
    private Long labId;

    private Long fromLabUserId;

    private Long toLabUserId;

    private Long labItemId;

    private Long labInId;

    private Integer status;

    private Long startTime;

    private Long endTime;

}
