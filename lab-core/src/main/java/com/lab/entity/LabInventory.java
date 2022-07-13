package com.lab.entity;

import com.baomidou.mybatisplus.annotation.Version;
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
public class LabInventory {

    private Long id;

    private Long labShelfId;

    private Long labInId;

    private Long capacityId;

    private Integer num;

    private Integer capacity;

    private Long labId;

    private Long labItemId;

    private Long labUserId;

    @Version
    private Integer version;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @Builder
    public static class Tid {

        private Long labShelfId;

        private Long labInId;

        private Long capacityId;
    }

    public Tid toTid() {
        return Tid.builder()
            .labShelfId(labShelfId)
            .labInId(labInId)
            .capacityId(capacityId)
            .build();
    }



}
