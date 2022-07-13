package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.LabOut;
import com.lab.entity.LabRole;
import org.apache.ibatis.annotations.Select;

public interface LabOutMapper extends BaseMapper<LabOut> {

    @Select("select exists(select id from lab_out where op_lab_shelf_id = #{opLabShelfId})")
    boolean existByOpLabShelfId(Long opLabShelfId);

}
