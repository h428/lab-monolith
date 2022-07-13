package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Lab;
import com.lab.entity.LabInventory;
import org.apache.ibatis.annotations.Select;

public interface LabInventoryMapper extends BaseMapper<LabInventory> {

    @Select("select exists(select id from lab_inventory where lab_shelf_id = #{labShelfId})")
    boolean existByLabShelfId(Long labShelfId);

}
