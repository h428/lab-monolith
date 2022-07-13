package com.lab.business.converter;

import com.lab.business.ro.BaseUserResetPasswordRO;
import com.lab.entity.BaseUser;
import com.lab.business.bo.BaseUserBO;
import com.lab.business.ro.BaseUserRegisterRO;
import com.lab.business.ro.BaseUserUpdatePasswordRO;
import com.lab.business.vo.BaseUserVO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BaseUserConverter {

    BaseUserConverter INSTANCE = Mappers.getMapper(BaseUserConverter.class);

    BaseUser registerRoToEntity(BaseUserRegisterRO baseUserRegisterRO);


    BaseUserBO entityToBo(BaseUser entity);

    BaseUserVO entityToVo(BaseUser entity);

    List<BaseUserVO> entityToVo(List<BaseUser> entity);

    BaseUserBO roToBo(BaseUserUpdatePasswordRO ro);
    BaseUserBO roToBo(BaseUserResetPasswordRO ro);

}
