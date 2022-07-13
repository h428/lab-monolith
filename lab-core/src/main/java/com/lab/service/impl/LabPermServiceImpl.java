package com.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.entity.LabPerm;
import com.lab.mapper.LabPermMapper;
import com.lab.service.LabPermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabPermServiceImpl extends ServiceImpl<LabPermMapper, LabPerm> implements LabPermService {

    String getAdminLabPerms() {
        return super.baseMapper.queryAdminPermissions();
    }

    String getAssistantLabPerms() {
        return super.baseMapper.queryAssistantPermissions();
    }

}
