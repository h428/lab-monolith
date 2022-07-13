package com.lab.business.ro;

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
public class LabUpdateRO {

    /**
     * 出于鉴权需要，将名称设置为 labId，在 labConverter 转化时需要使用注解映射转化规则以转化为 id
     */
    @NotNull(message = LabMessage.ID_NOT_NULL)
    private Long labId;

    private String name;

    private String descInfo;

}
