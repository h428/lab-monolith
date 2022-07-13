package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.LabIn;
import com.lab.entity.LabInventory;
import org.apache.ibatis.annotations.Select;

public interface LabInMapper extends BaseMapper<LabIn> {

    @Select("select exists(select id from lab_in where lab_item_id = #{labItemId})")
    boolean existByLabItemId(Long labItemId);

    @Select("select exists(select id from lab_in where op_lab_shelf_id = #{opLabShelfId})")
    boolean existByOpLabShelfId(Long opLabShelfId);

}
