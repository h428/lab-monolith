package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.LabUser;
import org.apache.ibatis.annotations.Select;

public interface LabUserMapper extends BaseMapper<LabUser> {

    /**
     * 根据 labRoleId 判断是否有有用使用该 labRoleId
     * @param labRoleId 角色 id
     * @return 存在返回 true，否则 false
     */
    @Select("select exists(select id from lab_user where lab_role_id = #{labRoleId})")
    boolean existByLabRoleId(Long labRoleId);

}
