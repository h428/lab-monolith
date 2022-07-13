package com.lab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.converter.LabOutConverter;
import com.lab.business.query.LabApplyQuery;
import com.lab.business.query.LabOutQuery;
import com.lab.business.vo.LabApplyVO;
import com.lab.business.vo.LabOutVO;
import com.lab.common.bean.PageBean;
import com.lab.common.component.MybatisPlusUtil;
import com.lab.common.query.PageQuery;
import com.lab.entity.LabApply;
import com.lab.entity.LabOut;
import com.lab.mapper.LabOutMapper;
import com.lab.service.LabOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabOutServiceImpl extends ServiceImpl<LabOutMapper, LabOut> implements LabOutService {

    private final LabOutConverter labOutConverter = LabOutConverter.INSTANCE;

    @Override
    public PageBean<LabOutVO> pageByQuery(LabOutQuery labOutQuery, PageQuery pageQuery) {
        Long startTime = labOutQuery.getStartTime();
        Long endTime = labOutQuery.getEndTime();
        LabOut labOut = this.labOutConverter.queryToEntity(labOutQuery);
        Page<LabOut> pageQ = MybatisPlusUtil.pageConvert(pageQuery);
        LambdaQueryWrapper<LabOut> query = Wrappers.lambdaQuery(labOut)
            .ge(startTime != null, LabOut::getOpTime, startTime)
            .le(endTime != null, LabOut::getOpTime, endTime)
            .orderByDesc(LabOut::getOpTime);
        Page<LabOut> pageRes = super.page(pageQ, query);
        return MybatisPlusUtil.pageConvert(pageRes, this.labOutConverter::entityToVo);
    }

    boolean existByLabShelfId(Long labShelfId) {
        return super.baseMapper.existByOpLabShelfId(labShelfId);
    }

}
