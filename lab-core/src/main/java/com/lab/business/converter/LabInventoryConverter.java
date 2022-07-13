package com.lab.business.converter;

import com.lab.business.query.LabInventoryQuery;
import com.lab.business.ro.LabCreateRO;
import com.lab.business.vo.LabInventoryVO;
import com.lab.business.vo.LabVO;
import com.lab.entity.Lab;
import com.lab.entity.LabInventory;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabInventoryConverter {

    LabInventoryConverter INSTANCE = Mappers.getMapper(LabInventoryConverter.class);

    LabInventoryVO entityToVo(LabInventory entity);

    List<LabInventoryVO> entityToVo(List<LabInventory> entityList);

    LabInventory queryToEntity(LabInventoryQuery query);

}
