package com.lab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.converter.LabUserConvert;
import com.lab.business.message.LabUserMessage;
import com.lab.business.ro.JoinRO;
import com.lab.business.ro.LabUserUpdateRO;
import com.lab.business.vo.LabRoleVO;
import com.lab.business.vo.LabUserVO;
import com.lab.common.bean.PageBean;
import com.lab.common.component.MybatisPlusUtil;
import com.lab.common.exception.ParamErrorException;
import com.lab.common.query.PageQuery;
import com.lab.common.util.ClassUtil;
import com.lab.common.util.MyAssert;
import com.lab.entity.BaseUser;
import com.lab.entity.Lab;
import com.lab.entity.LabJoinLink;
import com.lab.entity.LabRole;
import com.lab.entity.LabUser;
import com.lab.mapper.LabUserMapper;
import com.lab.service.LabUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabUserServiceImpl extends ServiceImpl<LabUserMapper, LabUser> implements LabUserService {


    private final LabUserConvert labUserConvert = LabUserConvert.INSTANCE;

    @Autowired
    private LabServiceImpl labService;

    @Autowired
    private LabRoleServiceImpl labRoleService;

    @Autowired
    private LabJoinLinkServiceImpl labJoinLinkService;

    @Autowired
    private BaseUserServiceImpl baseUserService;

    @Override
    public void joinByLink(JoinRO joinRO) {

        LabJoinLink labJoinLink = this.labJoinLinkService.getEntityByLink(joinRO.getLink());
        MyAssert.notNull(labJoinLink, "非法授权链接");

        Long baseUserId = joinRO.getBaseUserId();
        Long labId = labJoinLink.getLabId();
        Long labRoleId = labJoinLink.getLabRoleId();

        // 查询该用户原来是否已经加入过实验室
        LambdaQueryWrapper<LabUser> byLabIdAndBaseUserId = Wrappers.<LabUser>lambdaQuery()
            .eq(LabUser::getLabId, labId)
            .eq(LabUser::getBaseUserId, baseUserId);
        LabUser labUser = super.getOne(byLabIdAndBaseUserId);

        if (labUser == null) {
            // 若原来不存在则创建一个新对象
            labUser = LabUser.builder()
                .baseUserId(joinRO.getBaseUserId())
                .labId(labJoinLink.getLabId())
                .build();
        }

        if (labUser.getDeleted() != null && !labUser.getDeleted()) {
            // 该用户已存在该实验室，重复创建
            throw new ParamErrorException("已位于当前实验室，无需重复加入");
        }

        // 设置属性
        labUser.setLabRoleId(labRoleId);
        labUser.setName(joinRO.getLabUserName());

        // 时间创建人
        ClassUtil.setLabUserIdAndTime(labUser, 0L);
        labUser.setDeleted(false); // 若是已删除的用户取消删除状态

        // 执行保存或更新
        super.saveOrUpdate(labUser);
    }

    @Override
    public void fakeDeleteById(Long labUserId) {
        LabUser labUser = super.getById(labUserId);

        MyAssert.notNull(labUser, "实验室成员不存在");

        // 判断是否为实验室拥有者
        Lab lab = this.labService.getById(labUser.getLabId());
        if (lab.getBelongBaseUserId().equals(labUser.getBaseUserId())) {
            throw new ParamErrorException("无法删除实验室拥有者");
        }

        LabUser up = LabUser.builder()
            .id(labUserId)
            .deleted(true)
            .deleteTime(System.currentTimeMillis()).build();
        super.updateById(up);
    }

    @Override
    public void update(LabUserUpdateRO labUserUpdateRO) {
        LabUser labUser = this.labUserConvert.roToEntity(labUserUpdateRO);
        ClassUtil.setLabUserIdAndTime(labUser, labUserUpdateRO.getOpLabUserId());
        super.baseMapper.updateById(labUser);

    }

    @Override
    public void leave(Long labUserId) {
        // 确保 labUserId 对应的 baseUserId 不是实验室的拥有者
        LabUser labUser = super.getById(labUserId);
        MyAssert.notNull(labUser, LabUserMessage.NOT_EXIST);

        Long labId = labUser.getLabId();
        Lab lab = this.labService.getById(labId);
        MyAssert.notEquals(labUser.getBaseUserId(), lab.getBelongBaseUserId(), LabUserMessage.BELONG_USER_CANNOT_LEAVE);

        // 执行退出，即伪删除
        fakeDeleteById(labUserId);
    }

    @Override
    public LabUserVO get(Long id) {
        LabUser entity = super.getById(id);
        return this.labUserConvert.entityToVo(entity);
    }

    @Override
    public boolean save(LabUser entity) {
        return super.save(entity);
    }

    @Override
    public LabUserVO findAddedInVo(Long baseUserId, Long labId) {
        LabUserVO labUserVO = this.labUserConvert.entityToVo(findAddedIn(baseUserId, labId));

        if (labUserVO == null) {
            return null;
        }

        Long labRoleId = labUserVO.getLabRoleId();
        if (labRoleId == null) {
            return labUserVO;
        }

        LabRoleVO labRole = this.labRoleService.get(labRoleId);

        if (labRole == null) {
            // 直接返回
            return labUserVO;
        }

        // 继续处理 labRole 再返回
        labUserVO.setLabRole(labRole);

        // 添加 added-in 权限
        labRole.addAddedIn();

        // 酌情添加 own 权限
        if (this.own(baseUserId, labId)) {
            labRole.addOwn();
        }

        return labUserVO;
    }

    @Override
    public List<LabUserVO> listByIds(List<Long> ids) {
        List<LabUser> labUsers = super.listByIds(ids);
        return this.labUserConvert.entityToVo(labUsers);
    }

    @Override
    public List<LabUserVO> listByLabId(Long labId) {
        LabUser labUser = LabUser.builder().labId(labId).deleted(false).build();
        List<LabUser> labUsers = super.baseMapper.selectList(Wrappers.query(labUser));
        return this.labUserConvert.entityToVo(labUsers);
    }

    public PageBean<LabUserVO> pageByLabId(Long labId, PageQuery pageQuery) {
        LabUser labUser = LabUser.builder().labId(labId).build();
        Example<LabUser> example = Example.of(labUser);

        Page<LabUser> pageParam = Page.of(pageQuery.getPage(), pageQuery.getSize());

        Page<LabUser> pageRes = super.page(pageParam, Wrappers.query(labUser));
        return MybatisPlusUtil.pageConvert(pageParam, LabUserVO.class);
    }

    // package 级别方法，不开放给 controller 层，但提供给本层的其他 service 调用
    // 其他 service 直接引用实现类而不是接口，故直接以 Entity 作为传输对象而避免 VO 的转换损耗

    /**
     * 根据 baseUserId 和 labId 查询指定用户是否加入实验室
     * @param baseUserId 基础用户 id
     * @param labId 加入的实验室 id
     * @return 若用户已加入实验室，返回 labUser 对象；未加入则返回 null
     */
    LabUser findAddedIn(Long baseUserId, Long labId) {
        LabUser addedInQuery = LabUser.builder()
            .baseUserId(baseUserId)
            .labId(labId)
            .deleted(false)
            .build();
        return this.baseMapper.selectOne(Wrappers.query(addedInQuery));
    }

    /**
     * 根据 baseUserId 查询已加入的实验室列表
     * @param baseUserId 基础用户 id
     * @return 返回该用户已加入的实验室列表；没有则返回空列表
     */
    List<LabUser> findAddedInList(Long baseUserId) {
        LabUser addedInQuery = LabUser.builder()
            .baseUserId(baseUserId)
            .deleted(false)
            .build();
        return this.baseMapper.selectList(Wrappers.query(addedInQuery));
    }

    boolean own(Long baseUserId, Long labId) {
        Lab lab = this.labService.getById(labId);
        return  lab != null && lab.getBelongBaseUserId().equals(baseUserId);
    }

    boolean existByLabRoleId(Long labRoleId) {
        return super.baseMapper.existByLabRoleId(labRoleId);
    }

    /**
     * 为新创建的实验室的拥有者创建对应的实验室用户
     * @param entity 新建的实验室信息
     * @param admin 新建的 admin 角色
     * @param labUserId 提前生成的 labUserId，已被用于前面其他对象的创建，故此处要保持一致
     * @return 创建的实验室用户 labUser
     */
    LabUser initLabUserForNewLab(Lab entity, LabRole admin, Long labUserId) {

        Long labId = entity.getId();
        Long baseUserId = entity.getBelongBaseUserId();
        Long labRoleId = admin.getId();
        // 时间实验室的创建时间保持一致
        Long time = entity.getCreateTime();

        // 其中 name 和 baseUser 的名称保持一致，故查询出 baseUser
        BaseUser baseUser = this.baseUserService.getById(baseUserId);

        LabUser labUser = LabUser.builder()
            .labId(labId)
            .baseUserId(baseUserId)
            .name(baseUser.getUsername())
            .labRoleId(labRoleId)
            .build();
        // 设置 labUserId 和 time 时间，注意不要提前设置 id，否则会被当做更新不设置 create 相关属性
        ClassUtil.setLabUserIdAndTime(labUser, labUserId, time);
        labUser.setId(labUserId);

        // 保存
        super.save(labUser);

        return labUser;
    }
}
