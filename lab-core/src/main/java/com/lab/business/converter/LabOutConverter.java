package com.lab.business.converter;

import com.lab.business.query.LabOutQuery;
import com.lab.business.query.MyLabOutQuery;
import com.lab.business.vo.LabOutVO;
import com.lab.entity.LabOut;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabOutConverter {

    LabOutConverter INSTANCE = Mappers.getMapper(LabOutConverter.class);

    LabOutVO entityToVo(LabOut entity);

    List<LabOutVO> entityToVo(List<LabOut> entityList);

    LabOut queryToEntity(LabOutQuery query);

    LabOutQuery myOutQueryToLabOutQuery(MyLabOutQuery query);

}
