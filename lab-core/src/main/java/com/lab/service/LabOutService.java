package com.lab.service;

import com.lab.business.query.LabOutQuery;
import com.lab.business.vo.LabOutVO;
import com.lab.common.bean.PageBean;
import com.lab.common.query.PageQuery;

public interface LabOutService {

    PageBean<LabOutVO> pageByQuery(LabOutQuery labOutQuery, PageQuery pageQuery);

}
