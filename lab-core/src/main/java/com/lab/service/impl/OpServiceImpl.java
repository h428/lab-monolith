package com.lab.service.impl;

import cn.hutool.core.util.IdUtil;
import com.lab.business.constant.LabConstant;
import com.lab.business.message.LabMessage;
import com.lab.business.message.LabInventoryMessage;
import com.lab.business.message.LabItemMessage;
import com.lab.business.message.LabShelfMessage;
import com.lab.business.constant.MetaData;
import com.lab.business.enums.col.LabApplyStatusEnum;
import com.lab.business.enums.op.OpApplyResultEnum;
import com.lab.business.ro.OpApplyRO;
import com.lab.business.ro.OpConfirmRO;
import com.lab.business.ro.OpInRO;
import com.lab.business.ro.OpMoveRO;
import com.lab.business.ro.OpOutRO;
import com.lab.common.aop.param.ServiceValidated;
import com.lab.common.exception.BusinessException;
import com.lab.common.util.MyAssert;
import com.lab.entity.LabApply;
import com.lab.entity.LabIn;
import com.lab.entity.LabInventory;
import com.lab.entity.LabInventory.Tid;
import com.lab.entity.LabItem;
import com.lab.entity.LabOut;
import com.lab.entity.LabShelf;
import com.lab.service.OpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OpServiceImpl implements OpService {

    @Autowired
    private LabInventoryServiceImpl labInventoryService;

    @Autowired
    private LabOutServiceImpl labOutService;

    @Autowired
    private LabShelfServiceImpl labShelfService;

    @Autowired
    private LabApplyServiceImpl labApplyService;

    @Autowired
    private LabInServiceImpl labInService;

    @Autowired
    private LabItemServiceImpl labItemService;

    boolean isCapacityFull(LabInventory inventory) {
        // capacity 为容量，不等于 0 表示是容量品（例如 1 瓶盐酸 40 毫升），若是 0 则为普通物品，不具备容量属性
        // 该种情况下，capacityId 有值说明该行是拆封后的容量品记录行；为 0 则表示是完整未开封的容量瓶记录行
        if (inventory.getCapacity() != 0 && inventory.getCapacityId() == 0) {
            return true;
        }
        return false;
    }

    @Override
    @ServiceValidated
    public void in(OpInRO opInRO) {
        // 先查询 labItem
        LabItem labItem = this.labItemService.getById(opInRO.getLabItemId());
        MyAssert.notNull(labItem, LabItemMessage.NOT_EXIST);
        MyAssert.equals(opInRO.getLabId(), labItem.getLabId(), LabMessage.LAB_ID_DATA_ID_INCONSISTENCY);

        LabShelf labShelf = this.labShelfService.getById(opInRO.getLabShelfId());
        MyAssert.notNull(labShelf, LabShelfMessage.NOT_EXIST);
        MyAssert.equals(opInRO.getLabId(), labShelf.getLabId(), LabMessage.LAB_ID_DATA_ID_INCONSISTENCY);

        // 保存入库记录
        long labInId = IdUtil.getSnowflake().nextId();

        // 保存库存
        LabInventory inventory = LabInventory.builder()
            .labShelfId(labShelf.getId())
            .labInId(labInId)
            .num(opInRO.getNum())
            .capacity(labItem.getCapacity())
            .labId(labShelf.getLabId())
            .labItemId(labItem.getId())
            .labUserId(labShelf.getBelongLabUserId())
            .build();
        this.labInventoryService.save(inventory);


        // 保存入库记录
        LabIn labIn = LabIn.builder()
            .id(labInId)
            .labItemId(labItem.getId())
            .num(opInRO.getNum())
            .price(opInRO.getPrice())
            .opInfo(opInRO.getOpInfo())
            .opTime(System.currentTimeMillis())
            .opLabShelfId(labShelf.getId())
            .opLabUserId(opInRO.getOpLabUserId())
            .labId(opInRO.getLabId())
            .build();
        this.labInService.save(labIn);
    }


    @Override
    @ServiceValidated
    public void out(OpOutRO opOutRO) {
        Long id = opOutRO.getId();

        LabInventory inventory = this.labInventoryService.getById(id);
        MyAssert.notNull(inventory, LabInventoryMessage.OP_NUM_EXCEED_LEFT);

        if (isCapacityFull(inventory)) {
            // 本次做开封消耗
            outCapacityItem(inventory, opOutRO);
            return;
        }

        // 库存扣减
        minus(inventory.toTid(), opOutRO.getNum());

        // 保存消耗记录
        LabOut labOut = LabOut.builder()
            .labInId(inventory.getLabInId())
            .capacityId(inventory.getCapacityId())
            .num(opOutRO.getNum())
            .opInfo(opOutRO.getOpInfo())
            .opTime(System.currentTimeMillis())
            .opLabShelfId(inventory.getLabShelfId())
            .opLabUserId(opOutRO.getOpLabUserId())
            .labItemId(inventory.getLabItemId())
            .labId(inventory.getLabId())
            .build();
        this.labOutService.save(labOut);
    }

    void outCapacityItem(LabInventory inventory, OpOutRO opOutRO) {
        // 计算开封剩余量
        int newNum = inventory.getCapacity() - opOutRO.getNum();

        if (newNum < 0) {
            throw new BusinessException(LabInventoryMessage.OP_NUM_EXCEED_VOLUME);
        }

        // 库存 - 1（注意后 4 位是小数，故要减去 BASE）
        minus(inventory.toTid(), MetaData.INT_FLOAT_NUMBER_BASE);

        // 生成 capacityId，用于记录本次开封
        long capacityId = IdUtil.getSnowflake().nextId();

        // 开封消耗后还有剩余
        if (newNum > 0) {
            Tid tid = inventory.toTid().setCapacityId(capacityId);
            plus(tid, newNum, inventory);
        }

        // 保存消耗记录
        LabOut labOut = LabOut.builder()
            .labInId(inventory.getLabInId())
            .capacityId(capacityId)
            .num(opOutRO.getNum())
            .opInfo(opOutRO.getOpInfo())
            .opTime(System.currentTimeMillis())
            .opLabShelfId(inventory.getLabShelfId())
            .opLabUserId(opOutRO.getOpLabUserId())
            .labItemId(inventory.getLabItemId())
            .labId(inventory.getLabId())
            .build();
        labOutService.save(labOut);
    }

    @Override
    @ServiceValidated
    public OpApplyResultEnum apply(OpApplyRO opApplyRO) {
        Long id = opApplyRO.getId();
        Long toLabShelfId = opApplyRO.getToLabShelfId();
        LabInventory inventory = this.labInventoryService.getById(id);

        // 库存不足
        MyAssert.notNull(inventory, "库存不足");
        Long fromLabShelfId = inventory.getLabShelfId();

        // 申请操作，物品从 fromLabShelf 转移到 toLabShelf

        // 获取目标架子
        LabShelf toLabShelf = this.labShelfService.getById(toLabShelfId);
        MyAssert.notNull(toLabShelf, "目标架子不存在");

        // 保存申请记录
        LabApply labApply = LabApply.builder()
            .labInId(inventory.getLabInId())
            .capacityId(inventory.getCapacityId())
            .num(opApplyRO.getNum())
            .opInfo(opApplyRO.getOpInfo())
            .opTime(System.currentTimeMillis())
            .fromLabShelfId(fromLabShelfId)
            .toLabShelfId(toLabShelfId)
            .fromLabUserId(inventory.getLabUserId())
            .toLabUserId(toLabShelf.getBelongLabUserId())
            .labItemId(inventory.getLabItemId())
            .labId(inventory.getLabId())
            .status(LabApplyStatusEnum.TO_BE_CONFIRM.ordinal())
            .build();
        this.labApplyService.save(labApply);

        // 根据源架子的类型确定后续，若是公共架子则触发自动审批，若是其他架子则需要等待人员审批
        if (!this.labShelfService.autoConfirm(fromLabShelfId)) {
            // 无需自动审批，保存记录单后就直接返回
            return OpApplyResultEnum.TO_BE_CONFIRM;
        }

        // 需要审批则继续执行审批操作
        OpConfirmRO opConfirmRO = OpConfirmRO.builder()
            .labApplyId(labApply.getId())
            .accept(true)
            .confirmLabUserId(LabConstant.SYSTEM_LAB_USER_ID)
            .build();

        // 若自动审批失败，则抛出异常以回滚前面的记录保存操作
        confirm(opConfirmRO);
        return OpApplyResultEnum.CONFIRMED;
    }

    @Override
    @ServiceValidated
    public void move(OpMoveRO opMoveRO) {
        doMove(opMoveRO, true);
    }


    @Override
    @ServiceValidated
    public void confirm(OpConfirmRO opConfirmRO) {

        Long labApplyId = opConfirmRO.getLabApplyId();
        Boolean accept = opConfirmRO.getAccept();

        int status = accept ? LabApplyStatusEnum.ACCEPT.ordinal() :
            LabApplyStatusEnum.REJECT.ordinal();

        // 先更新记录单状态
        LabApply reject = LabApply.builder()
            .id(labApplyId)
            .confirmLabUserId(opConfirmRO.getConfirmLabUserId())
            .confirmTime(System.currentTimeMillis())
            .status(status).build();
        this.labApplyService.updateById(reject);

        if (!accept) {
            // 若审批意见为拒绝，则只需修改状态就结束
            return;
        }

        // 否则审批意见为同意，执行审批操作

        LabApply labApply = this.labApplyService.getById(labApplyId);

        Long fromLabShelfId = labApply.getFromLabShelfId();
        Long toLabShelfId = labApply.getToLabShelfId();
        Long labInId = labApply.getLabInId();
        Integer num = labApply.getNum();
        Long labId = labApply.getLabId();

        // 直接执行移动操作
        OpMoveRO opMoveRO = OpMoveRO.builder()
            .fromLabShelfId(fromLabShelfId)
            .toLabShelfId(toLabShelfId)
            .labInId(labInId)
            .labId(labId)
            .capacityId(labApply.getCapacityId())
            .num(num)
            .build();
        doMove(opMoveRO, false);
    }

    void doMove(OpMoveRO opMoveRO, boolean saveMoveRecord) {
        Integer num = opMoveRO.getNum();

        Tid fromTid = opMoveRO.fromTid();
        Tid toTid = opMoveRO.toTid();

        LabInventory from = minus(fromTid, num);
        // 相当于权限重验，确保传入的 labId 是正确的
        MyAssert.equals(from.getLabId(), opMoveRO.getLabId(), "实验室 id 不一致");

        LabInventory to = plus(toTid, num, from);

        if (saveMoveRecord) {
            long time = System.currentTimeMillis();
            // 保存移动操作对应 labApply 记录
            LabApply labApplyOfMove = LabApply.builder()
                .labInId(fromTid.getLabInId())
                .capacityId(fromTid.getCapacityId())
                .num(num)
                .opInfo(opMoveRO.getOpInfo())
                .opTime(time)
                .confirmTime(time)
                .fromLabShelfId(from.getLabShelfId())
                .toLabShelfId(to.getLabShelfId())
                .fromLabUserId(from.getLabUserId())
                .toLabUserId(to.getLabUserId())
                .confirmLabUserId(opMoveRO.getOpLabUserId())
                .move(true)
                .labItemId(from.getLabItemId())
                .labId(from.getLabId())
                .status(LabApplyStatusEnum.ACCEPT.ordinal())
                .build();
            this.labApplyService.save(labApplyOfMove);
        }
    }

    LabInventory minus(Tid tid, int num) {
        LabInventory labInventory = this.labInventoryService.getByTid(tid);

        // 源架子已被全部消耗，库存不足，无法移动
        MyAssert.notNull(labInventory, "库存不足");

        // 源架子还有库存剩余，但库存不足
        int newNum = labInventory.getNum() - num;
        MyAssert.isTrue(newNum >= 0, "库存不足");

        // 移除源架子库存

        if (newNum == 0) {
            // 若剩余量为 0，则直接删除
            if (!this.labInventoryService.deleteByIdAndVersion(labInventory.getId(), labInventory.getVersion())) {
                throw new BusinessException("请重试");
            }
            return labInventory;
        }

        // 若还有剩余量，则更新为剩余量
        LabInventory updateById = LabInventory.builder()
            .id(labInventory.getId())
            .num(newNum)
            .version(labInventory.getVersion())
            .build();
        if (!labInventoryService.updateById(updateById)) {
            // 乐观锁更新失败，表示数据有更新，提示重试
            throw new BusinessException("请重试");
        }

        return labInventory;
    }

    LabInventory plus(Tid tid, Integer num, LabInventory from) {
        LabInventory labInventory = this.labInventoryService.getByTid(tid);

        Long labShelfId = tid.getLabShelfId();
        Long labInId = tid.getLabInId();

        if (labInventory == null) {
            // 查询出 labShelf 以获取 labUserId
            LabShelf labShelf = this.labShelfService.getById(labShelfId);
            MyAssert.notNull(labShelf, "目标架子不存在");
            Long labUserId = labShelf.getBelongLabUserId();

            // 若目标架子原来不存在 labInId 对应库存，则 insert 一条库存记录
            LabInventory save = LabInventory.builder()
                .labShelfId(labShelfId)
                .labInId(labInId)
                .capacityId(tid.getCapacityId())
                .num(num)
                .capacity(from.getCapacity())
                .labItemId(from.getLabItemId())
                .labId(from.getLabId())
                .labUserId(labUserId)
                .build();
            labInventoryService.save(save);
            return save;
        }

        // 更新
        LabInventory update = LabInventory.builder()
            .id(labInventory.getId())
            .version(labInventory.getVersion())
            .num(num + labInventory.getNum())
            .build();
        if (!labInventoryService.updateById(update)) {
            throw new BusinessException("请重试");
        }
        return update;
    }


}
