package com.lab.business.converter;

import com.lab.business.ro.LabUserUpdateRO;
import com.lab.entity.LabUser;
import com.lab.business.vo.LabUserVO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabUserConvert {

    LabUserConvert INSTANCE = Mappers.getMapper(LabUserConvert.class);

    LabUserVO entityToVo(LabUser entity);

    List<LabUserVO> entityToVo(List<LabUser> entityList);

    LabUser roToEntity(LabUserUpdateRO labUserUpdateRO);

}
