package com.lab.service;

import com.lab.business.query.LabInQuery;
import com.lab.business.vo.LabInVO;
import com.lab.common.bean.PageBean;
import com.lab.common.query.PageQuery;
import java.util.List;

public interface LabInService {


    List<LabInVO> listByIds(List<Long> idList);

    PageBean<LabInVO> page(LabInQuery labInQuery, PageQuery pageQuery);


}
