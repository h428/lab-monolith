package com.lab.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.constant.MetaData;
import com.lab.business.converter.LabJoinLinkConverter;
import com.lab.business.ro.LabJoinLinkCreateRO;
import com.lab.business.vo.LabJoinLinkVO;
import com.lab.entity.Lab;
import com.lab.entity.LabJoinLink;
import com.lab.entity.LabRole;
import com.lab.mapper.LabJoinLinkMapper;
import com.lab.service.LabJoinLinkService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabJoinLinkServiceImpl extends ServiceImpl<LabJoinLinkMapper, LabJoinLink> implements LabJoinLinkService {

    private final LabJoinLinkConverter labJoinLinkConverter = LabJoinLinkConverter.INSTANCE;

    @Autowired
    private LabServiceImpl labService;

    @Autowired
    private LabRoleServiceImpl labRoleService;

    @Override
    public void create(LabJoinLinkCreateRO labCreateRO) {
        LabJoinLink labJoinLink = this.labJoinLinkConverter.createRoToEntity(labCreateRO);
        String link = RandomUtil.randomString(MetaData.LINK_LENGTH);
        labJoinLink.setLink(link);
        labJoinLink.setCreateTime(System.currentTimeMillis());
        labJoinLink.setCreateLabUserId(labCreateRO.getOpLabUserId());
        super.save(labJoinLink);
    }

    @Override
    public void deleteById(Long id) {
        super.removeById(id);
    }

    @Override
    public LabJoinLinkVO get(Long id) {
        return labJoinLinkConverter.entityToVo(super.getById(id));
    }

    private LabJoinLinkVO cascadeQuery(LabJoinLinkVO labJoinLinkVO) {

        Long labId = labJoinLinkVO.getLabId();
        Long labRoleId = labJoinLinkVO.getLabRoleId();

        Lab lab = this.labService.getById(labId);
        labJoinLinkVO.setLabName(lab.getName());

        LabRole labRole = this.labRoleService.getById(labRoleId);
        labJoinLinkVO.setLabRoleName(labRole.getName());


        return labJoinLinkVO;
    }

    @Override
    public LabJoinLinkVO getByLink(String link) {

        LabJoinLink entity = getEntityByLink(link);

        if (entity == null) {
            return LabJoinLinkVO.EMPTY;
        }

        LabJoinLinkVO labJoinLinkVO = this.labJoinLinkConverter.entityToVo(entity);

        // 级联查询
        return cascadeQuery(labJoinLinkVO);
    }

    @Override
    public List<LabJoinLinkVO> listByLabId(Long labId) {
        LabJoinLink query = LabJoinLink.builder().labId(labId).build();
        return this.labJoinLinkConverter.entityToVo(super.list(Wrappers.query(query)));
    }

    boolean existByLink(String link) {
        return super.baseMapper.existByLink(link);
    }

    public LabJoinLink getEntityByLink(String link) {
        LabJoinLink query = LabJoinLink.builder().link(link).build();
        return super.getOne(Wrappers.query(query));
    }
}
