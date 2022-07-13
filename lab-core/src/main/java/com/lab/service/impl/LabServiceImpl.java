package com.lab.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.converter.LabConverter;
import com.lab.business.ro.LabCreateRO;
import com.lab.business.ro.LabUpdateRO;
import com.lab.business.vo.LabEntryVO;
import com.lab.business.vo.LabEntryVO.LabEntryVOBuilder;
import com.lab.business.vo.LabVO;
import com.lab.entity.Lab;
import com.lab.entity.LabRole;
import com.lab.entity.LabUser;
import com.lab.mapper.LabMapper;
import com.lab.service.LabService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabServiceImpl extends ServiceImpl<LabMapper, Lab> implements LabService {

    private final LabConverter labConverter = LabConverter.INSTANCE;

    @Autowired
    private LabUserServiceImpl labUserService;

    @Autowired
    private LabRoleServiceImpl labRoleService;

    @Autowired
    private LabShelfServiceImpl labShelfService;

    @Autowired
    private LabItemServiceImpl labItemService;

    @Override
    public void create(LabCreateRO labCreateRO) {

        Lab entity = this.labConverter.roToEntity(labCreateRO);

        Long createUserId = labCreateRO.getCreateBaseUserId();
        long now = System.currentTimeMillis();

        entity.setBelongBaseUserId(createUserId);
        entity.setCreateTime(now);
        entity.setUpdateBaseUserId(createUserId);
        entity.setUpdateTime(now);

        super.save(entity);

        initLab(entity);
    }

    /**
     * 创建实验室后，为该实验室做初始化工作，主要包括：
     * 1. 创建 3 个默认角色，会同时创建对应的授权链接
     * 2. 创建拥有者对应的 labUser，并赋值角色为管理员，要求其同时创建一个个人架子
     * 3. 创建一个默认的公共架子和一个贮藏架子
     * 4. 创建一个默认的非容量品和一个默认的容量品，以便于测试
     * @param entity 实验室对象
     */
    private void initLab(Lab entity) {

        // 先生成一个 labUserId，在创建 labRole 和授权链接过程中要用到该值，
        // 同时后续创建 labUser 时就要使用这一 id 以保持一致性
        long labUserId = IdUtil.getSnowflake().nextId();

        // 做实验室角色初始化并拿到 admin
        LabRole admin = this.labRoleService.initLabRoleForNewLab(entity, labUserId);

        // 基于 admin 角色创建 labUser
        LabUser labUser = this.labUserService.initLabUserForNewLab(entity, admin, labUserId);

        // 创建三类默认架子
        this.labShelfService.initLabShelfForNewLab(entity, labUser);

        // 创建两个默认物品
        this.labItemService.initLabItemForNewLab(entity, labUser);

    }


    /**
     * 根据 id 删除 Lab
     *
     * @param id 必须传入合法 id，非法 id 会抛出异常
     */
    @Override
    public void fakeDeleteById(Long id) {
        Lab lab = super.getById(id);
        lab.setDeleted(true);
        lab.setDeleteTime(System.currentTimeMillis());
    }

    @Override
    public void update(LabUpdateRO labUpdateRO) {
        Lab lab = this.labConverter.roToEntity(labUpdateRO);
        super.updateById(lab);
    }

    /**
     * 根据 id 查询 Lab 信息
     *
     * @param id 必须传入合法 id，非法 id 会抛出异常
     * @return 实验室信息
     */
    @Override
    public LabVO get(Long id) {
        Lab entity = super.getById(id);
        return this.labConverter.entityToVo(entity);
    }

    @Override
    public List<LabVO> listByIds(List<Long> idList) {
        List<Lab> labs = super.listByIds(idList);
        return this.labConverter.entityToVo(labs);
    }

    @Override
    public List<LabVO> listByBelongUserId(Long belongUserId) {
        Lab entity = Lab.builder().belongBaseUserId(belongUserId).build();
        List<Lab> labList = super.list(Wrappers.query(entity));
        return this.labConverter.entityToVo(labList);
    }

    @Override
    public Map<Long, LabEntryVO> findOwnLabEntryMap(Long baseUserId) {
        Lab queryByBelongUserId = Lab.builder().belongBaseUserId(baseUserId).build();
        List<Lab> labList = super.list(Wrappers.query(queryByBelongUserId));

        // 将 list 转换为 map 并返回
        return labList.stream()
            .collect(Collectors.toMap(Lab::getId, lab -> {

                Long labId = lab.getId();
                LabEntryVOBuilder builder = LabEntryVO.builder()
                    .labId(labId);

                // 查询用户和角色;
                LabUser addedInUser = this.labUserService.findAddedIn(baseUserId, labId);
                if (addedInUser != null) {
                    builder.labUserId(addedInUser.getId());
                    builder.labRoleId(addedInUser.getLabRoleId());
                    builder.labUserName(addedInUser.getName());
                }

                return builder.build();
            }));
    }

    @Override
    public Map<Long, LabEntryVO> findAddedInLabEntryMap(Long baseUserId) {
        List<LabUser> addedInList = this.labUserService.findAddedInList(baseUserId);
        return addedInList.stream()
            .collect(Collectors.toMap(LabUser::getLabId, labUser -> LabEntryVO.builder()
                .labId(labUser.getLabId())
                .labUserId(labUser.getId())
                .labUserName(labUser.getName())
                .labRoleId(labUser.getLabRoleId())
                .build()
            ));
    }
}
