package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.LabApply;
import com.lab.entity.LabJoinLink;
import org.apache.ibatis.annotations.Select;

public interface LabJoinLinkMapper extends BaseMapper<LabJoinLink> {

    @Select("select exists(select id from lab_join_link where link = #{link})")
    boolean existByLink(String link);

}
