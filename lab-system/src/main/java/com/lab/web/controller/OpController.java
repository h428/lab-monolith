package com.lab.web.controller;

import com.lab.business.constant.LabConstant;
import com.lab.business.enums.op.OpApplyResultEnum;
import com.lab.business.message.LabApplyMessage;
import com.lab.business.ro.OpApplyRO;
import com.lab.business.ro.OpConfirmRO;
import com.lab.business.ro.OpInRO;
import com.lab.business.ro.OpMoveRO;
import com.lab.business.ro.OpOutRO;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabApplyVO;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.common.exception.NoPermissionException;
import com.lab.common.util.MyAssert;
import com.lab.service.LabApplyService;
import com.lab.service.LabInventoryService;
import com.lab.service.LabItemService;
import com.lab.service.OpService;
import com.lab.web.perm.PermRecheck;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.LabPerm;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/op")
@Api("库存操作相关请求")
public class OpController {

    @Autowired
    private OpService opService;

    @Autowired
    private LabInventoryService labInventoryService;

    @Autowired
    private LabItemService labItemService;

    @Autowired
    private LabApplyService labApplyService;

    @PostMapping("in")
    @LabPerm(StrPerm.OP_IN)
    public ResBean<Void> in(@Validated @RequestBody OpInRO opInRO) {

        // 权限重验
        PermRecheck.recheckByDataId(opInRO.getLabItemId(), this.labItemService::get);

        Long labUserId = LabPermContext.labUserId();
        opInRO.setOpLabUserId(labUserId);

        this.opService.in(opInRO);
        return ResBean.ok_200("入库成功");
    }

    @PostMapping("out")
    public ResBean<Void> out(@Validated @RequestBody OpOutRO opOutRO) {
        opOutRO.setOpLabUserId(LabPermContext.labUserId());
        this.opService.out(opOutRO);
        return ResBean.ok_200("消耗成功");
    }

    @PostMapping("apply")
    public ResBean<Void> apply(@Validated @RequestBody OpApplyRO opApplyRO) {

        OpApplyResultEnum result = this.opService.apply(opApplyRO);
        if (result == OpApplyResultEnum.TO_BE_CONFIRM) {
            return ResBean.ok_200("申请成功，等待审批");
        }

        return ResBean.ok_200("申请成功");
    }

    @PostMapping("move")
    @LabPerm(StrPerm.OP_MOVE)
    public ResBean<Void> move(@Validated @RequestBody OpMoveRO opMoveRO) {
        opMoveRO.setOpLabUserId(LabPermContext.labUserId());
        this.opService.move(opMoveRO);
        return ResBean.ok_200("消耗成功");
    }

    private void permCheckOfConfirm(OpConfirmRO opConfirmRO) {

        // 权限重验
        LabApplyVO labApplyVO = PermRecheck.recheckByDataId(opConfirmRO.getLabApplyId(),
            this.labApplyService::get);

        // 同时根据 labApply 是向私人用户申请，还是向共用贮藏架子申请，进一步判定权限
        if (labApplyVO.getFromLabUserId().equals(LabConstant.SYSTEM_LAB_USER_ID)) {

            // 校验必须具有实验室审批权限
            if (!LabPermContext.hasPerm(StrPerm.OP_CONFIRM_COMMON_APPLY)) {
                throw new NoPermissionException();
            }
            return;
        }

        // 私人架子则要求是从当前用户申请才可审批
        MyAssert.equals(labApplyVO.getFromLabUserId(), LabPermContext.labUserId(), LabApplyMessage.NOT_MY_RECORD);
    }

    @PostMapping("confirm")
    @AddedIn
    public ResBean<Void> confirm(@Validated @RequestBody OpConfirmRO opConfirmRO) {
        permCheckOfConfirm(opConfirmRO);

        opConfirmRO.setConfirmLabUserId(LabPermContext.labUserId());
        this.opService.confirm(opConfirmRO);
        return ResBean.ok_200("移动成功");
    }

}
