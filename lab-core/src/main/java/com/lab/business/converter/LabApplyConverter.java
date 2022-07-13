package com.lab.business.converter;

import com.lab.business.query.LabApplyQuery;
import com.lab.business.ro.LabCreateRO;
import com.lab.business.vo.LabApplyVO;
import com.lab.business.vo.LabVO;
import com.lab.entity.Lab;
import com.lab.entity.LabApply;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabApplyConverter {

    LabApplyConverter INSTANCE = Mappers.getMapper(LabApplyConverter.class);

    LabApplyVO entityToVo(LabApply entity);

    List<LabApplyVO> entityToVo(List<LabApply> entityList);

    LabApply queryToEntity(LabApplyQuery query);

}
