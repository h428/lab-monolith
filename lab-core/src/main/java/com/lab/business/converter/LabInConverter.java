package com.lab.business.converter;

import com.lab.business.query.LabInQuery;
import com.lab.business.vo.LabInVO;
import com.lab.entity.LabIn;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabInConverter {

    LabInConverter INSTANCE = Mappers.getMapper(LabInConverter.class);

    LabInVO entityToVo(LabIn entity);

    List<LabInVO> entityToVo(List<LabIn> entityList);

    LabIn queryToEntity(LabInQuery labInQuery);

}
