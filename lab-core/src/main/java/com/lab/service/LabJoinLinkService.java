package com.lab.service;

import com.lab.business.ro.LabCreateRO;
import com.lab.business.ro.LabJoinLinkCreateRO;
import com.lab.business.vo.LabJoinLinkVO;
import java.util.List;

public interface LabJoinLinkService {

    void create(LabJoinLinkCreateRO labJoinLinkCreateRO);

    void deleteById(Long id);

    LabJoinLinkVO get(Long id);
    /**
     * 根据 link 查询 LabJoinLink，
     * @param link 加入链接
     * @return LabJoinLink；若不存在返回一个 id 为 -1 的空对象而不是 null
     */
    LabJoinLinkVO getByLink(String link);

    List<LabJoinLinkVO> listByLabId(Long labId);

}
