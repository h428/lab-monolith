package com.lab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.converter.LabInConverter;
import com.lab.business.query.LabInQuery;
import com.lab.business.vo.LabInVO;
import com.lab.common.bean.PageBean;
import com.lab.common.component.MybatisPlusUtil;
import com.lab.common.query.PageQuery;
import com.lab.entity.LabIn;
import com.lab.mapper.LabInMapper;
import com.lab.service.LabInService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabInServiceImpl extends ServiceImpl<LabInMapper, LabIn> implements LabInService {

    private final LabInConverter labInConverter = LabInConverter.INSTANCE;

    @Override
    public List<LabInVO> listByIds(List<Long> idList) {
        List<LabIn> labIns = super.baseMapper.selectBatchIds(idList);
        return this.labInConverter.entityToVo(labIns);
    }

    @Override
    public PageBean<LabInVO> page(LabInQuery labInQuery, PageQuery pageQuery) {

        Long startTime = labInQuery.getStartTime();
        Long endTime = labInQuery.getEndTime();

        LabIn labIn = labInConverter.queryToEntity(labInQuery);
        Page<LabIn> pageQ = MybatisPlusUtil.pageConvert(pageQuery);
        LambdaQueryWrapper<LabIn> query = Wrappers.lambdaQuery(labIn)
            .ge(startTime != null, LabIn::getOpTime, startTime)
            .le(endTime != null, LabIn::getOpTime, endTime)
            .orderByDesc(LabIn::getOpTime);
        Page<LabIn> pageRes = super.page(pageQ, query);
        return MybatisPlusUtil.pageConvert(pageRes, this.labInConverter::entityToVo);
    }

    boolean existByLabItemId(Long labItemId) {
        return super.baseMapper.existByLabItemId(labItemId);
    }

    boolean existByLabShelfId(Long labShelfId) {
        return super.baseMapper.existByOpLabShelfId(labShelfId);
    }
}
