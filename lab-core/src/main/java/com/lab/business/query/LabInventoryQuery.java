package com.lab.business.query;

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
public class LabInventoryQuery {

    @NotNull(message = LabInventoryMessage.LAB_ID_NOT_NULL)
    private Long labId;

    private Long labShelfId;

    private Long labUserId;

    private Long labItemId;

    private String search;
}
