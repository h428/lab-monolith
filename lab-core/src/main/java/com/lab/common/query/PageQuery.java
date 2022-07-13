package com.lab.common.query;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author hao
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
public class PageQuery {

    @ApiModelProperty("页码")
    @Builder.Default
    @Positive(message = "页码必须为正数")
    private int page = 1; // 默认查询第一页

    @ApiModelProperty("每页的数量")
    @Builder.Default
    @Positive(message = "每页记录数必须为正数")
    @Max(value = 100, message = "每页大小不得超过 100")
    private int size = 10; // 默认每页 10 条

    @ApiModelProperty("排序字段列表")
    private List<Order> orders;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Accessors(chain = true)
    @Builder
    @ToString
    public static class Order {
        private String property;

        @Builder.Default
        private boolean desc = false; // 默认升序
    }

}
