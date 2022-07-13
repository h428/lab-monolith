package com.lab.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.business.converter.LabInventoryConverter;
import com.lab.business.query.LabInventoryQuery;
import com.lab.business.vo.LabInventoryVO;
import com.lab.common.bean.PageBean;
import com.lab.common.component.MybatisPlusUtil;
import com.lab.common.query.PageQuery;
import com.lab.common.util.MyAssert;
import com.lab.entity.Lab;
import com.lab.entity.LabInventory;
import com.lab.entity.LabItem;
import com.lab.entity.LabShelf;
import com.lab.entity.LabUser;
import com.lab.mapper.LabInventoryMapper;
import com.lab.service.LabInventoryService;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabInventoryServiceImpl extends ServiceImpl<LabInventoryMapper, LabInventory> implements LabInventoryService {

    private final LabInventoryConverter labInventoryConverter = LabInventoryConverter.INSTANCE;

    @Autowired
    private LabShelfServiceImpl labShelfService;

    @Autowired
    private LabItemServiceImpl labItemService;

    @Autowired
    private LabUserServiceImpl labUserService;

    @Autowired
    private LabServiceImpl labService;

    public PageBean<LabInventoryVO> search(LabInventoryQuery labInventoryQuery, PageQuery pageQuery) {
        Long labId = labInventoryQuery.getLabId();
        Long labShelfId = labInventoryQuery.getLabShelfId();
        Long labUserId = labInventoryQuery.getLabUserId();
        String search = labInventoryQuery.getSearch();

        // 先根据 labId 和 key 确定 labItems 集合
        String matchSql = String.format("and match(name, eng_name, desc_info) against('%s')", search);
        Wrapper<LabItem> labItemWrapper = Wrappers.<LabItem>lambdaQuery()
            .eq(LabItem::getLabId, labId)
            .last(matchSql);
        List<LabItem> labItems = this.labItemService.list(labItemWrapper);

        if (labItems.isEmpty()) {
            return PageBean.empty(pageQuery);
        }

        List<Long> labItemIdList = labItems.stream()
            .map(LabItem::getId).collect(Collectors.toList());

        Page<LabInventory> page = MybatisPlusUtil.pageConvert(pageQuery);

        // 同时加上 labShelfId 和 labUserId 作为可选过滤条件
        LambdaQueryWrapper<LabInventory> inventoryWrapper = Wrappers.<LabInventory>lambdaQuery()
            .eq(LabInventory::getLabId, labId)
            .in(LabInventory::getLabItemId, labItemIdList)
            .eq(labShelfId != null, LabInventory::getLabShelfId, labShelfId)
            .eq(labUserId != null, LabInventory::getLabUserId, labUserId);

        Page<LabInventory> pageRes = super.page(page, inventoryWrapper);
        return MybatisPlusUtil.pageConvert(pageRes, this.labInventoryConverter::entityToVo);

    }

    @Override
    public PageBean<LabInventoryVO> page(LabInventoryQuery labInventoryQuery, PageQuery pageQuery) {

        // 将 PageQuery 转化为 mybatis 的分页对象
        Page<LabInventory> mPage = MybatisPlusUtil.pageConvert(pageQuery);

        Long labId = labInventoryQuery.getLabId();

        // 优先根据 search 做查询（会组合其他 id 条件）
        String search = labInventoryQuery.getSearch();
        if (StrUtil.isNotBlank(search)) {
            return search(labInventoryQuery, pageQuery);
        }


        // 1. 根据 labShelfId 查询
        Long labShelfId = labInventoryQuery.getLabShelfId();
        if (labShelfId != null) {
            return this.pageByLabShelfId(labShelfId, labId, mPage);
        }

        // 2. 根据 labItemId 查询
        Long labItemId = labInventoryQuery.getLabItemId();
        if (labItemId != null) {
            return this.pageByLabItemId(labItemId, labId, mPage);
        }

        // 3. 根据 labUserId 查询
        Long labUserId = labInventoryQuery.getLabUserId();
        if (labUserId != null) {
            return this.pageByLabUserId(labUserId, labId, mPage);
        }


        // 4. 根据 search 查询


        // 5. 根据 labId 查询
        return this.pageByLabId(labId, mPage);
    }


    LabInventory getByTid(LabInventory.Tid tid) {
        LabInventory query = LabInventory.builder()
            .labShelfId(tid.getLabShelfId())
            .labInId(tid.getLabInId())
            .capacityId(tid.getCapacityId())
            .build();
        return super.getOne(Wrappers.query(query));
    }

    @Deprecated
    PageBean<LabInventoryVO> pageByLabShelfIdOld(Long labShelfId, Long labId, Page<LabInventory> mPage) {
        LabShelf labShelf = this.labShelfService.getById(labShelfId);
        MyAssert.notNull(labShelf, "实验室架子不存在");
        MyAssert.equals(labShelf.getLabId(), labId, "实验室 id 不一致");
        LabInventory byLabShelf = LabInventory.builder().labShelfId(labShelfId).build();
        Page<LabInventory> mRes = super.page(mPage, Wrappers.query(byLabShelf));
        return MybatisPlusUtil.pageConvert(mRes, labInventoryConverter::entityToVo);
    }

    @Deprecated
    PageBean<LabInventoryVO> pageByLabItemIdOld(Long labItemId, Long labId, Page<LabInventory> mPage) {
        LabItem labItem = this.labItemService.getById(labItemId);
        MyAssert.notNull(labItem, "实验室物品不存在");
        MyAssert.equals(labItem.getLabId(), labId, "实验室 id 不一致");
        LabInventory byLabItem = LabInventory.builder().labItemId(labItemId).build();
        Page<LabInventory> mRes = super.page(mPage, Wrappers.query(byLabItem));
        return MybatisPlusUtil.pageConvert(mRes, labInventoryConverter::entityToVo);
    }

    @Deprecated
    PageBean<LabInventoryVO> pageByLabUserIdOld(Long labUserId, Long labId, Page<LabInventory> mPage) {
        LabUser labUser = this.labUserService.getById(labUserId);
        MyAssert.notNull(labUser, "实验室用户不存在");
        MyAssert.equals(labUser.getLabId(), labId, "实验室 id 不一致");
        LabInventory byLabItem = LabInventory.builder().labItemId(labUserId).build();
        Page<LabInventory> mRes = super.page(mPage, Wrappers.query(byLabItem));
        return MybatisPlusUtil.pageConvert(mRes, this.labInventoryConverter::entityToVo);
    }

    PageBean<LabInventoryVO> pageByLabShelfId(Long labShelfId, Long labId, Page<LabInventory> mPage) {
        return pageByDataId(this.labShelfService::getById, LabShelf::getLabId,
            LabInventory::getLabShelfId, labShelfId, labId, mPage);
    }

    PageBean<LabInventoryVO> pageByLabItemId(Long labItemId, Long labId, Page<LabInventory> mPage) {
        return pageByDataId(this.labItemService::getById, LabItem::getLabId,
            LabInventory::getLabItemId, labItemId, labId, mPage);
    }

    PageBean<LabInventoryVO> pageByLabUserId(Long labUserId, Long labId, Page<LabInventory> mPage) {
        return pageByDataId(this.labUserService::getById, LabUser::getLabId,
            LabInventory::getLabUserId, labUserId, labId, mPage);
    }

    PageBean<LabInventoryVO> pageByLabId(Long labId, Page<LabInventory> mPage) {
        // 根据 labId 查会有一不多余的判断，即根据 id 查询出 lab 后，又会调用 lab.getId 和 labId 做比较
        // 但影响不大
        return pageByDataId(this.labService::getById, Lab::getId,
            LabInventory::getLabId, labId, labId, mPage);
    }

    /**
     * 抽象方法，根据 xxxId 分页查询；
     * 提取三个 pageByLabXxxId 方法共性，抽象到极限就会形成该方法，但会导致可读性下降，多人协作项目慎用；
     * 出于可读性需要，保留原有的三个方法并标记为 @Deprecated 以便分析逻辑；
     * @param getById 原有的 this.xxxService.getById 的抽取，传入对应方法（赋值时绑定）
     * @param getLabId 原有的 labXxx.getLabId() 的抽取，传入对应方法，以在查询出数据后调用方法（调用时绑定）
     * @param getDataId 原有的 labInventory.getXxxId() 的抽取，用于构造 wrapper（调用时绑定）
     * @param dataId 数据值，根据 labShelfId/labItemId/labUserId 查询
     * @param labId 实验室 id，用于查询出 labShelf/labItem/labUser 后，归属的 labId 数据一致（相当于二次鉴权）
     * @param mPage MybatisPlus 分页对象
     * @param <E> 数据类型，目前为 LabShelf/LabItem/LabUser
     * @return 库存分页对象
     */
    <E> PageBean<LabInventoryVO> pageByDataId(Function<Long, E> getById, Function<E, Long> getLabId,
        SFunction<LabInventory, Long> getDataId,
        Long dataId, Long labId, Page<LabInventory> mPage) {
        E data = getById.apply(dataId);
        MyAssert.notNull(data, String.format("数据 %d 不存在", dataId));
        MyAssert.equals(getLabId.apply(data), labId, "实验室 id 不一致");
        LambdaQueryWrapper<LabInventory> wrapper = Wrappers.<LabInventory>lambdaQuery()
            .eq(getDataId, dataId);
        Page<LabInventory> mRes = super.page(mPage, wrapper);
        return MybatisPlusUtil.pageConvert(mRes, this.labInventoryConverter::entityToVo);
    }

    boolean deleteByIdAndVersion(Long id, Integer version) {
        LabInventory del = LabInventory.builder().id(id).version(version).build();
        return super.remove(Wrappers.query(del));
    }

    boolean existByLabShelfId(Long labShelfId) {
        return super.baseMapper.existByLabShelfId(labShelfId);
    }

}
