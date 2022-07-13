package com.lab.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.constant.LabConstant;
import com.lab.business.converter.LabRoleConverter;
import com.lab.business.ro.LabJoinLinkCreateRO;
import com.lab.business.ro.LabRoleCreateRO;
import com.lab.business.ro.LabRoleUpdateRO;
import com.lab.business.vo.LabRoleVO;
import com.lab.common.aop.param.ServiceValidated;
import com.lab.common.exception.BusinessException;
import com.lab.common.util.ClassUtil;
import com.lab.entity.Lab;
import com.lab.entity.LabJoinLink;
import com.lab.entity.LabRole;
import com.lab.mapper.LabRoleMapper;
import com.lab.service.LabRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabRoleServiceImpl extends ServiceImpl<LabRoleMapper, LabRole> implements LabRoleService {

    private final LabRoleConverter labRoleConverter = LabRoleConverter.INSTANCE;

    @Autowired
    private LabUserServiceImpl labUserService;

    @Autowired
    private LabJoinLinkServiceImpl labJoinLinkService;

    @Autowired
    private LabPermServiceImpl labPermService;

    @Override
    @ServiceValidated
    public void create(LabRoleCreateRO labRoleCreateRO) {
        LabRole labRole = this.labRoleConverter.roToEntity(labRoleCreateRO);
        ClassUtil.setLabUserIdAndTime(labRole, labRoleCreateRO.getOpLabUserId());
        super.save(labRole);
    }

    @Override
    public void delete(Long id) {
        if (isUsing(id)) {
            throw new BusinessException("该角色使用中");
        }

        super.removeById(id);

        // 根据 labRoleId 级联删除
        cascadeDelete(id);
    }

    private void cascadeDelete(Long labRoleId) {
        LabJoinLink del = LabJoinLink.builder().labRoleId(labRoleId).build();
        labJoinLinkService.remove(Wrappers.query(del));
    }

    @Override
    @ServiceValidated
    public void update(LabRoleUpdateRO labRoleUpdateRO) {
        LabRole labRole = this.labRoleConverter.roToEntity(labRoleUpdateRO);
        labRole.setLabId(null);
        ClassUtil.setLabUserIdAndTime(labRole, labRoleUpdateRO.getOpLabUserId());
        super.updateById(labRole);
    }

    @Override
    public LabRoleVO get(Long id) {
        return this.labRoleConverter.entityToVo(this.getById(id));
    }

    @Override
    public String findPerms(Long labRoleId) {

        LabRole labRole = this.getById(labRoleId);

        if (labRole == null) {
            return "";
        }

        return labRole.getLabPerms();
    }

    @Override
    public List<LabRoleVO> listByIds(List<Long> idList) {
        List<LabRole> roles = super.listByIds(idList);
        return this.labRoleConverter.entityToVo(roles);
    }

    @Override
    public List<LabRoleVO> listByLabId(Long labId) {
        LabRole labRole = LabRole.builder().labId(labId).build();
        List<LabRole> labRoles = super.list(Wrappers.query(labRole));
        return this.labRoleConverter.entityToVo(labRoles);
    }

    boolean isUsing(Long labRoleId) {
        return this.labUserService.existByLabRoleId(labRoleId);
    }

    /**
     * 为新建的实验室初始化角色：管理员、助理员、普通成员
     * @param entity 刚新建的实验室
     * @return 返回 admin 用于后续创建用户
     */
    LabRole initLabRoleForNewLab(Lab entity, Long labUserId) {

        // 时间和实验室的创建时间保持一致
        long time = entity.getCreateTime();
        Long labId = entity.getId();

        String adminLabPerms = this.labPermService.getAdminLabPerms();
        LabRole admin = LabRole.builder()
            .name("管理员")
            .descInfo("管理员具备所有可用权限")
            .labPerms(adminLabPerms)
            .labId(labId)
            .build();
        // 由于 labRole 和 labUser 互相依赖
        // 此处用户先设置为系统用户 0，再后续创建 labUser 完毕后需要回写
        ClassUtil.setLabUserIdAndTime(admin, labUserId, time);
        super.save(admin);
        // 创建 admin 对应的授权链接
        LabJoinLinkCreateRO adminJoinLink = LabJoinLinkCreateRO.builder()
            .labRoleId(admin.getId())
            .labId(labId)
            .opLabUserId(labUserId)
            .build();
        this.labJoinLinkService.create(adminJoinLink);

        String assistantLabPerms = this.labPermService.getAssistantLabPerms();
        LabRole assistant = LabRole.builder()
            .name("助理员")
            .descInfo("助理员具备常规实验室管理权限，但不具备权限管理能力")
            .labPerms(assistantLabPerms)
            .labId(labId)
            .build();
        // 由于 labRole 和 labUser 互相依赖
        // 此处用户先设置为系统用户 0，再后续创建 labUser 完毕后需要回写
        ClassUtil.setLabUserIdAndTime(assistant, labUserId, time);
        super.save(assistant);
        // 创建 assistant 对应的授权链接
        LabJoinLinkCreateRO assistantJoinLink = LabJoinLinkCreateRO.builder()
            .labRoleId(assistant.getId())
            .labId(labId)
            .opLabUserId(labUserId)
            .build();
        this.labJoinLinkService.create(assistantJoinLink);

        LabRole member = LabRole.builder()
            .name("普通成员")
            .descInfo("普通成员无额外管理权限，只可使用实验室的库存以及查询个人情况")
            .labPerms("")
            .labId(labId)
            .build();
        // 由于 labRole 和 labUser 互相依赖
        // 此处用户先设置为系统用户 0，再后续创建 labUser 完毕后需要回写
        ClassUtil.setLabUserIdAndTime(member, LabConstant.SYSTEM_LAB_USER_ID, time);
        super.save(member);
        // 创建 member 对应的授权链接
        LabJoinLinkCreateRO memberJoinLink = LabJoinLinkCreateRO.builder()
            .labRoleId(member.getId())
            .labId(labId)
            .opLabUserId(labUserId)
            .build();
        this.labJoinLinkService.create(memberJoinLink);

        return admin;
    }
}
