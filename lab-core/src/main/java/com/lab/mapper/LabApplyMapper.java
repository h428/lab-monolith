package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Lab;
import com.lab.entity.LabApply;
import org.apache.ibatis.annotations.Select;

public interface LabApplyMapper extends BaseMapper<LabApply> {

    @Select("select exists(select id from lab_apply where from_lab_shelf_id = #{fromLabShelfId})")
    boolean existByFromLabShelfId(Long fromLabShelfId);

    @Select("select exists(select id from lab_apply where to_lab_shelf_id = #{toLabShelfId})")
    boolean existByToLabShelfId(Long toLabShelfId);

}
