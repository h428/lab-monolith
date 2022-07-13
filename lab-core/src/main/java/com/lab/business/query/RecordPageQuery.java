package com.lab.business.query;

import com.lab.common.exception.ParamErrorException;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用于记录单（HubIn、HubOut、HubApplyBack）的分页查询，封装了可选条件
 *
 * @author hao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class RecordPageQuery {

    @Builder.Default
    private int pageNum = 1; // 默认查询第一页

    @Max(value = 100L, message = "每页最多允许 100 条记录")
    @Builder.Default
    private int pageSize = 10; // 默认每页 10 条

    @NotNull(message = "起始时间不能为空")
    private Long startTime;

    @NotNull(message = "终止时间不能为空")
    private Long endTime;

    @Builder.Default
    private boolean desc = true; // 默认按时间降序

    /**
     * 校验时间跨度不得超过一年
     */
    public void validateTimeNotExceedOneYear() {
        if (endTime - startTime > 31622410000L) {
            throw new ParamErrorException("时间跨度不得超过一年");
        }
    }
}
