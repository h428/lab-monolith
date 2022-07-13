package com.lab.business.converter;

import com.lab.business.ro.LabJoinLinkCreateRO;
import com.lab.business.vo.LabJoinLinkVO;
import com.lab.entity.LabJoinLink;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabJoinLinkConverter {

    LabJoinLinkConverter INSTANCE = Mappers.getMapper(LabJoinLinkConverter.class);

    LabJoinLinkVO entityToVo(LabJoinLink entity);

    List<LabJoinLinkVO> entityToVo(List<LabJoinLink> entityList);

    LabJoinLink createRoToEntity(LabJoinLinkCreateRO createRO);

}
