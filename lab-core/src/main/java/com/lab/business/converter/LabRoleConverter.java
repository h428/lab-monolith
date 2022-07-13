package com.lab.business.converter;

import com.lab.business.ro.LabRoleCreateRO;
import com.lab.business.ro.LabRoleUpdateRO;
import com.lab.business.vo.LabRoleVO;
import com.lab.entity.LabRole;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabRoleConverter {

    LabRoleConverter INSTANCE = Mappers.getMapper(LabRoleConverter.class);

    LabRoleVO entityToVo(LabRole entity);

    List<LabRoleVO> entityToVo(List<LabRole> entityList);

    LabRole roToEntity(LabRoleCreateRO ro);

    LabRole roToEntity(LabRoleUpdateRO ro);

}
