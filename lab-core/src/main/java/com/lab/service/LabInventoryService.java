package com.lab.service;

import com.lab.business.query.LabInventoryQuery;
import com.lab.business.vo.LabInventoryVO;
import com.lab.common.bean.PageBean;
import com.lab.common.query.PageQuery;

public interface LabInventoryService {

    PageBean<LabInventoryVO> page(LabInventoryQuery labInventoryQuery, PageQuery pageQuery);
}
