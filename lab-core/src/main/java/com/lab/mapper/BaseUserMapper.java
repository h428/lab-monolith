package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.BaseUser;
import org.apache.ibatis.annotations.Select;

public interface BaseUserMapper extends BaseMapper<BaseUser> {

    /**
     * 根据 id 判断是否存在对应记录
     * @param email 邮箱
     * @return 存在返回 true，否则 false
     */
    @Select("select exists(select id from base_user where email = #{email})")
    boolean existByEmail(String email);

    @Select("select exists(select id from base_user where username = #{username})")
    boolean existByUsername(String username);

}
