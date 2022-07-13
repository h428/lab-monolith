package com.lab.service;

import com.lab.business.ro.LabShelfCreateRO;
import com.lab.business.ro.LabShelfUpdateRO;
import com.lab.business.vo.LabShelfVO;
import com.lab.common.constant.Group.Service;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

public interface LabShelfService {

    void create(LabShelfCreateRO labShelfCreateRO);

    void delete(Long id);

    void update(LabShelfUpdateRO labShelfUpdateRO);

    LabShelfVO get(Long id);

    List<LabShelfVO> listByIds(List<Long> idList);

    List<LabShelfVO> listByLabUserId(Long labUserId);

    List<LabShelfVO> listByLabId(Long labId);



}
