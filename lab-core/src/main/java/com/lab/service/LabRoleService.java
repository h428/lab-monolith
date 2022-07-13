package com.lab.service;

import com.lab.business.ro.LabRoleCreateRO;
import com.lab.business.ro.LabRoleUpdateRO;
import com.lab.business.vo.LabRoleVO;
import com.lab.common.aop.param.ServiceValidated;
import java.util.List;
import javax.validation.Valid;


public interface LabRoleService {

    @ServiceValidated
    void create(@Valid LabRoleCreateRO labRoleCreateRO);

    void delete(Long id);

    @ServiceValidated
    void update(@Valid LabRoleUpdateRO labRoleUpdateRO);

    LabRoleVO get(Long id);

    String findPerms(Long labRoleId);

    List<LabRoleVO> listByIds(List<Long> idList);

    List<LabRoleVO> listByLabId(Long labId);



}
