package com.lab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.converter.LabApplyConverter;
import com.lab.business.query.LabApplyQuery;
import com.lab.business.vo.LabApplyVO;
import com.lab.common.bean.PageBean;
import com.lab.common.component.MybatisPlusUtil;
import com.lab.common.query.PageQuery;
import com.lab.entity.LabApply;
import com.lab.mapper.LabApplyMapper;
import com.lab.service.LabApplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabApplyServiceImpl extends ServiceImpl<LabApplyMapper, LabApply> implements LabApplyService {

    private final LabApplyConverter labApplyConverter = LabApplyConverter.INSTANCE;

    @Override
    public LabApplyVO get(Long labApplyId) {
        return this.labApplyConverter.entityToVo(super.getById(labApplyId));
    }

    @Override
    public PageBean<LabApplyVO> pageByQuery(LabApplyQuery labApplyQuery, PageQuery pageQuery) {
        Long startTime = labApplyQuery.getStartTime();
        Long endTime = labApplyQuery.getEndTime();
        LabApply labApply = this.labApplyConverter.queryToEntity(labApplyQuery);
        Page<LabApply> pageQ = MybatisPlusUtil.pageConvert(pageQuery);
        LambdaQueryWrapper<LabApply> query = Wrappers.lambdaQuery(labApply)
            .ge(startTime != null, LabApply::getOpTime, startTime)
            .le(endTime != null, LabApply::getOpTime, endTime)
            .orderByDesc(LabApply::getOpTime);
        Page<LabApply> pageRes = super.page(pageQ, query);
        return MybatisPlusUtil.pageConvert(pageRes, this.labApplyConverter::entityToVo);
    }

    boolean existByLabShelfId(Long labShelfId) {
        boolean from = super.baseMapper.existByFromLabShelfId(labShelfId);
        if (from) return true;

        return super.baseMapper.existByToLabShelfId(labShelfId);
    }

}
