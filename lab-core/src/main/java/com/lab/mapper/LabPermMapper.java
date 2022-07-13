package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.LabPerm;
import org.apache.ibatis.annotations.Select;

public interface LabPermMapper extends BaseMapper<LabPerm> {

    /**
     * 查询管理员角色的所有权限的字符串（各个权限使用 , 连接）
     * @return
     */
    @Select(value = "select group_concat(tag) from lab_perm")
    String queryAdminPermissions();

    /**
     * 查询助手角色的字符串权限（各个权限使用 , 连接）
     * @return
     */
    @Select(value = "select group_concat(tag) from lab_perm where id != 104 and pid != 103")
    String queryAssistantPermissions();

}
