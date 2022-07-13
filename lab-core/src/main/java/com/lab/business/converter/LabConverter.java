package com.lab.business.converter;

import com.lab.business.ro.LabCreateRO;
import com.lab.business.ro.LabUpdateRO;
import com.lab.business.vo.LabVO;
import com.lab.entity.Lab;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabConverter {

    LabConverter INSTANCE = Mappers.getMapper(LabConverter.class);

    LabVO entityToVo(Lab entity);

    List<LabVO> entityToVo(List<Lab> entityList);

    Lab roToEntity(LabCreateRO ro);

    @Mapping(source = "labId", target = "id")
    Lab roToEntity(LabUpdateRO ro);

}
