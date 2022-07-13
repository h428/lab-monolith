package com.lab.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class LabIn {

    private Long id;

    private Long labItemId;

    private Integer num;

    private Integer price;

    private String opInfo;

    private Long opTime;

    private Long opLabShelfId;

    private Long opLabUserId;

    private Long labId;

    private Boolean deleted;

    private Long deleteTime;

}
