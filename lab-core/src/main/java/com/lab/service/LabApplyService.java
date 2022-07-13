package com.lab.service;

import com.lab.business.query.LabApplyQuery;
import com.lab.business.vo.LabApplyVO;
import com.lab.business.vo.LabInVO;
import com.lab.common.bean.PageBean;
import com.lab.common.query.PageQuery;
import java.util.List;

public interface LabApplyService {

    LabApplyVO get(Long labApplyId);

    PageBean<LabApplyVO> pageByQuery(LabApplyQuery labApplyQuery, PageQuery pageQuery);



}
