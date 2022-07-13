package com.lab.service;

import com.lab.business.ro.JoinRO;
import com.lab.business.ro.LabUserUpdateRO;
import com.lab.business.vo.LabUserVO;
import java.util.List;

public interface LabUserService {

    /**
     * 根据 link 加入实验室，相当于 create；
     * 如果以加入过被删除则不修改 labUserId，而是恢复对应的实验室 id
     * @param joinRO
     */
    void joinByLink(JoinRO joinRO);

    void fakeDeleteById(Long labUserId);

    void update(LabUserUpdateRO labUserUpdateRO);

    void leave(Long labUserId);

    LabUserVO get(Long id);

    LabUserVO findAddedInVo(Long baseUserId, Long labId);

    List<LabUserVO> listByIds(List<Long> ids);

    List<LabUserVO> listByLabId(Long labId);

}
