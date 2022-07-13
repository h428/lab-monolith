package com.lab.common.bean;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.lab.common.query.PageQuery;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageBean<T> implements Serializable {

    private long total; // 总记录数
    private int pages; // 总页数
    // 请求的当前页
    private int page;
    // 请求的每页的数量
    private int size;
    // 当前页最终的数量（一般最后一页数组不足时才有用，否则一般和 pageSize 保持一致）
    private List<T> list;



    public <E> PageBean<E> convert(Class<E> clazz) {
        List<E> data = BeanUtil.copyToList(list, clazz);
        return PageBean.<E>builder()
            .total(this.total)
            .pages(this.pages)
            .page(this.page)
            .size(this.size)
            .list(data)
            .build();
    }

    public static <E> PageBean<E> empty(PageQuery pageQuery) {
        return PageBean.<E>builder()
            .total(0)
            .pages(0)
            .page(pageQuery.getPage())
            .size(pageQuery.getSize())
            .list(Lists.newArrayList())
            .build();
    }

}
