package com.lab.business.converter;

import com.lab.business.ro.LabShelfCreateRO;
import com.lab.business.ro.LabShelfUpdateRO;
import com.lab.business.ro.MyLabShelfCreateRO;
import com.lab.business.vo.LabShelfVO;
import com.lab.entity.LabShelf;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabShelfConverter {

    LabShelfConverter INSTANCE = Mappers.getMapper(LabShelfConverter.class);

    LabShelfVO entityToVo(LabShelf entity);

    List<LabShelfVO> entityToVo(List<LabShelf> entityList);

    LabShelf roToEntity(LabShelfCreateRO ro);

    LabShelf roToEntity(LabShelfUpdateRO ro);

    LabShelfCreateRO convert(MyLabShelfCreateRO ro);

}
