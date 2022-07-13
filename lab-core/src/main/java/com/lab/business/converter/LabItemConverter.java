package com.lab.business.converter;

import com.lab.business.ro.LabItemCreateRO;
import com.lab.business.ro.LabItemUpdateRO;
import com.lab.business.vo.LabItemVO;
import com.lab.entity.LabItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabItemConverter {

    LabItemConverter INSTANCE = Mappers.getMapper(LabItemConverter.class);

    LabItemVO entityToVo(LabItem entity);

    List<LabItemVO> entityToVo(List<LabItem> entityList);

    LabItem roToEntity(LabItemCreateRO saveRO);

    LabItem roToEntity(LabItemUpdateRO saveRO);

}
