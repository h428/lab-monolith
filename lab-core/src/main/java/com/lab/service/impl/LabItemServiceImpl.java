package com.lab.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.converter.LabItemConverter;
import com.lab.business.ro.LabItemCreateRO;
import com.lab.business.ro.LabItemUpdateRO;
import com.lab.business.vo.LabItemVO;
import com.lab.common.aop.param.ServiceValidated;
import com.lab.common.util.ClassUtil;
import com.lab.common.util.MyAssert;
import com.lab.entity.Lab;
import com.lab.entity.LabIn;
import com.lab.entity.LabItem;
import com.lab.entity.LabUser;
import com.lab.mapper.LabItemMapper;
import com.lab.service.LabItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabItemServiceImpl extends ServiceImpl<LabItemMapper, LabItem> implements LabItemService {

    private final LabItemConverter labItemConverter = LabItemConverter.INSTANCE;

    @Autowired
    private LabInServiceImpl labInService;

    @Override
    public void create(LabItemCreateRO labItemCreateRO) {
        LabItem labItem = this.labItemConverter.roToEntity(labItemCreateRO);
        ClassUtil.setLabUserIdAndTime(labItem, labItemCreateRO.getOpLabUserId());
        super.save(labItem);
    }

    @Override
    public void delete(Long id) {
        // 如果 labItem 入库过，说明有使用数据，则采用伪删除
        if (this.labInService.existByLabItemId(id)) {
            fakeDelete(id);
            return;
        }

        // 否则采用物理删除
        super.removeById(id);
    }

    @Override
    @ServiceValidated
    public void update(LabItemUpdateRO labItemUpdateRO) {
        LabItem labItem = this.labItemConverter.roToEntity(labItemUpdateRO);
        // labId 字段不做更新，故移除 labId 字段
        labItem.setLabId(null);
        ClassUtil.setLabUserIdAndTime(labItem, labItemUpdateRO.getOpLabUserId());
        super.updateById(labItem);
    }

    void fakeDelete(Long id) {
        LabItem labItem = LabItem.builder()
            .id(id)
            .deleted(true)
            .deleteTime(System.currentTimeMillis())
            .build();
        super.updateById(labItem);
    }

    @Override
    public LabItemVO get(Long id) {
        return this.labItemConverter.entityToVo(super.getById(id));
    }

    @Override
    public List<LabItemVO> listByIds(List<Long> idList) {
        List<LabItem> items = super.listByIds(idList);
        return this.labItemConverter.entityToVo(items);
    }

    @Override
    public List<LabItemVO> listByLabId(Long labId) {
        LabItem query = LabItem.builder().labId(labId).deleted(false).build();
        List<LabItem> items = super.list(Wrappers.query(query));
        return this.labItemConverter.entityToVo(items);
    }

    LabItem getByLabInId(Long labInId) {
        LabIn labIn = this.labInService.getById(labInId);
        MyAssert.notNull(labIn, "labInId 不存在");
        return super.getById(labIn.getLabItemId());
    }

    /**
     * 为新建的实验室创建两个默认的物品，一个容量品，一个非容量品
     * @param entity
     * @param labUser
     */
    void initLabItemForNewLab(Lab entity, LabUser labUser) {

        Long labId = entity.getId();
        Long labUserId = labUser.getId();
        Long time = entity.getCreateTime();

        LabItem commonItem = LabItem.builder()
            .name("烧瓶")
            .descInfo("非容量品样本")
            .price(1100)
            .unit("个")
            .floated(false)
            .labId(labId)
            .capacity(0)
            .capacityUnit("")
            .build();
        ClassUtil.setLabUserIdAndTime(commonItem, labUserId, time);
        super.save(commonItem);


        LabItem capacityItem = LabItem.builder()
            .name("某品牌小瓶盐酸")
            .descInfo("容量品样本：容量品指的是必须按容量消耗的物品，必须为其设置容量和容量单位，后续的使用中按容量消耗。"
                + "例如，本盐酸按瓶入库，一瓶盐酸有 100 毫升，做实验按毫升消耗，记录以毫升为单位记录消耗")
            .price(2200)
            .unit("个")
            .floated(true)
            .labId(labId)
            .capacity(1000000)
            .capacityUnit("毫升")
            .build();
        ClassUtil.setLabUserIdAndTime(capacityItem, labUserId, time);
        super.save(capacityItem);

    }
}
