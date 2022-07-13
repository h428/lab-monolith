package com.lab.service;


import com.lab.business.ro.BaseUserLoginRO;
import com.lab.business.ro.BaseUserRegisterRO;
import com.lab.business.ro.BaseUserUpdatePasswordRO;
import com.lab.business.ro.BaseUserUpdateRO;
import com.lab.business.vo.BaseUserVO;
import java.util.List;

public interface BaseUserService {

    void register(BaseUserRegisterRO baseUserRegisterRO);

    BaseUserVO loginByEmail(BaseUserLoginRO baseUserLoginRO);

    void update(BaseUserUpdateRO baseUserUpdateRO);

    void updatePassword(BaseUserUpdatePasswordRO passwordDto);

    void resetPassword(BaseUserUpdatePasswordRO passwordDto);

    BaseUserVO get(Long id);

    boolean existByEmail(String email);

    boolean existByUsername(String username);

    List<BaseUserVO> listByIds(List<Long> ids);
}
