package com.lab.service;

import com.lab.business.ro.LabItemCreateRO;
import com.lab.business.ro.LabItemUpdateRO;
import com.lab.business.vo.LabItemVO;
import com.lab.common.aop.param.ServiceValidated;
import java.util.List;
import javax.validation.Valid;

public interface LabItemService {

    void create(LabItemCreateRO labItemCreateRO);

    void delete(Long id);

    @ServiceValidated
    void update(@Valid LabItemUpdateRO labItemUpdateRO);

    LabItemVO get(Long id);

    List<LabItemVO> listByIds(List<Long> idList);

    List<LabItemVO> listByLabId(Long labId);


}
