package com.lab.service;

import com.lab.business.ro.LabCreateRO;
import com.lab.business.ro.LabUpdateRO;
import com.lab.business.vo.LabEntryVO;
import com.lab.business.vo.LabVO;
import com.lab.common.constant.Group.Service;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

public interface LabService {

    void create(LabCreateRO labCreateRO);

    void fakeDeleteById(Long id);

    void update(LabUpdateRO labUpdateRO);

    LabVO get(Long id);

    List<LabVO> listByIds(List<Long> idList);

    List<LabVO> listByBelongUserId(Long belongUserId);

    Map<Long, LabEntryVO> findOwnLabEntryMap(Long baseUserId);

    Map<Long, LabEntryVO> findAddedInLabEntryMap(Long baseUserId);

}
