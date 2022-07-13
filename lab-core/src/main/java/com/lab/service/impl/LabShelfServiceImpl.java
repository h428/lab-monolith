package com.lab.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.constant.LabConstant;
import com.lab.business.converter.LabShelfConverter;
import com.lab.business.enums.col.LabShelfTypeEnum;
import com.lab.business.ro.LabShelfCreateRO;
import com.lab.business.ro.LabShelfUpdateRO;
import com.lab.business.vo.LabShelfVO;
import com.lab.common.exception.BusinessException;
import com.lab.common.util.ClassUtil;
import com.lab.entity.Lab;
import com.lab.entity.LabShelf;
import com.lab.entity.LabUser;
import com.lab.mapper.LabShelfMapper;
import com.lab.service.LabShelfService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabShelfServiceImpl extends ServiceImpl<LabShelfMapper, LabShelf> implements LabShelfService {

    private final LabShelfConverter labShelfConverter = LabShelfConverter.INSTANCE;

    @Autowired
    private LabInServiceImpl labInService;

    @Autowired
    private LabOutServiceImpl labOutService;

    @Autowired
    private LabApplyServiceImpl labApplyService;

    @Autowired
    private LabInventoryServiceImpl labInventoryService;

    @Override
    public void create(LabShelfCreateRO labShelfCreateRO) {
        LabShelf labShelf = this.labShelfConverter.roToEntity(labShelfCreateRO);


        // 非私人架子设置所属用户 id 为 0
        if (labShelf.getType() != LabShelfTypeEnum.PRIVATE.ordinal()) {
            labShelf.setBelongLabUserId(0L);
        }
        ClassUtil.setLabUserIdAndTime(labShelf, labShelfCreateRO.getOpLabUserId());
        super.saveOrUpdate(labShelf);
    }

    @Override
    public void delete(Long id) {
        if (using(id)) {
            throw new BusinessException("该架子还有库存，无法删除");
        }

        if (used(id)) {
            // 伪删除
            fakeDelete(id);
            return;
        }

        removeById(id);
    }

    private void fakeDelete(Long id) {
        LabShelf del = LabShelf.builder()
            .id(id)
            .deleted(true)
            .deleteTime(System.currentTimeMillis())
            .build();
        super.updateById(del);
    }

    @Override
    public void update(LabShelfUpdateRO labShelfUpdateRO) {
        LabShelf labShelf = this.labShelfConverter.roToEntity(labShelfUpdateRO);
        // 不允许更新 labId，但 labShelfUpdateRO 中有 labId 用于鉴权，故置空
        labShelf.setLabId(null);
        super.updateById(labShelf);
    }

    @Override
    public LabShelfVO get(Long id) {
        return this.labShelfConverter.entityToVo(super.getById(id));
    }

    @Override
    public List<LabShelfVO> listByIds(List<Long> idList) {
        List<LabShelf> items = super.baseMapper.selectBatchIds(idList);
        return this.labShelfConverter.entityToVo(items);
    }

    @Override
    public List<LabShelfVO> listByLabUserId(Long labUserId) {
        LabShelf query = LabShelf.builder().belongLabUserId(labUserId).deleted(false).build();
        List<LabShelf> labShelves = super.baseMapper.selectList(Wrappers.query(query));
        return this.labShelfConverter.entityToVo(labShelves);
    }

    @Override
    public List<LabShelfVO> listByLabId(Long labId) {
        LabShelf labShelf = LabShelf.builder().labId(labId).deleted(false).build();
        List<LabShelf> labShelves = super.baseMapper.selectList(Wrappers.query(labShelf));
        return this.labShelfConverter.entityToVo(labShelves);
    }

    /**
     * 根据 labShelfId 判断该架子是否需要自动审批
     * @param labShelfId
     * @return
     */
    boolean autoConfirm(Long labShelfId) {
        LabShelf labShelf = super.baseMapper.selectById(labShelfId);
        return this.autoConfirm(labShelf);
    }

    /**
     * 根据给定的 labShelf 对象判断该架子是否需要自动审批
     * @param labShelf
     * @return
     */
    boolean autoConfirm(LabShelf labShelf) {
        if (labShelf == null) {
            return false;
        }

        // 若是 Public 类型架子则需要自动审批
        return Objects.equals(labShelf.getType(), LabShelfTypeEnum.PUBLIC.ordinal());
    }

    /**
     * 根据 labShelfId 判断架子是否使用中
     * @param labShelfId
     * @return
     */
    boolean using(Long labShelfId) {
        // 如果架子上还有库存，则表示在使用中
        return labInventoryService.existByLabShelfId(labShelfId);
    }

    boolean used(Long labShelfId) {
        // 如果消耗记录中记录过 labShelfId
        if (this.labOutService.existByLabShelfId(labShelfId)) {
            return true;
        }

        if (labInService.existByLabShelfId(labShelfId)) {
            return true;
        }

        return labApplyService.existByLabShelfId(labShelfId);
    }

    /**
     * 为新建的实验室创建架子，包括私人架子、公共架子、贮藏架子各一个
     * @param entity 新建的实验室对象
     * @param labUser 新建的实验室用户
     */
    void initLabShelfForNewLab(Lab entity, LabUser labUser) {

        Long labId = entity.getId();
        Long labUserId = labUser.getId();
        Long time = entity.getCreateTime();

        // 私人架子
        LabShelf privateLabShelf = LabShelf.builder()
            .name(labUser.getName() + "的默认架子")
            .type(LabShelfTypeEnum.PRIVATE.ordinal())
            .belongLabUserId(labUserId)
            .labId(labId)
            .build();
        ClassUtil.setLabUserIdAndTime(privateLabShelf, labUserId, time);
        super.save(privateLabShelf);

        // 私人架子
        LabShelf publicLabShelf = LabShelf.builder()
            .name("默认公共架子")
            .type(LabShelfTypeEnum.PUBLIC.ordinal())
            .belongLabUserId(LabConstant.SYSTEM_LAB_USER_ID)
            .labId(labId)
            .build();
        ClassUtil.setLabUserIdAndTime(publicLabShelf, labUserId, time);
        super.save(publicLabShelf);

        // 私人架子
        LabShelf storageLabShelf = LabShelf.builder()
            .name("默认贮藏架子")
            .type(LabShelfTypeEnum.STORAGE.ordinal())
            .belongLabUserId(LabConstant.SYSTEM_LAB_USER_ID)
            .labId(labId)
            .build();
        ClassUtil.setLabUserIdAndTime(storageLabShelf, labUserId, time);
        super.save(storageLabShelf);


    }
}
