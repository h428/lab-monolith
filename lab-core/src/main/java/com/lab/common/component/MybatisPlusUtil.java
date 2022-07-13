package com.lab.common.component;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.common.bean.PageBean;
import com.lab.common.query.PageQuery;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.poi.ss.formula.functions.T;

public class MybatisPlusUtil {

    public static <E, T> PageBean<T> pageConvert(Page<E> pageMybatis, Function<List<E>, List<T>> converter) {
        List<E> records = pageMybatis.getRecords();
        List<T> data = converter.apply(records);
        return PageBean.<T>builder()
            .page((int) pageMybatis.getCurrent())
            .size((int) pageMybatis.getSize())
            .total(pageMybatis.getTotal())
            .pages((int)pageMybatis.getPages())
            .list(data)
            .build();
    }

    public static <T> PageBean<T> pageConvert(Page<?> pageMybatis, Class<T> clazz) {
        List<?> records = pageMybatis.getRecords();
        List<T> data = BeanUtil.copyToList(records, clazz);
        return PageBean.<T>builder()
            .page((int) pageMybatis.getCurrent())
            .size((int) pageMybatis.getSize())
            .total(pageMybatis.getTotal())
            .pages((int)pageMybatis.getPages())
            .list(data)
            .build();
    }

    public static <T> Page<T> pageConvert(PageQuery pageQuery) {
        return Page.of(pageQuery.getPage(), pageQuery.getSize());
    }



}
