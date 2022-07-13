package com.lab.web.controller;

import com.lab.business.enums.col.LabApplyStatusEnum;
import com.lab.business.query.LabApplyQuery;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabApplyVO;
import com.lab.common.bean.PageBean;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.common.query.PageQuery;
import com.lab.entity.LabApply;
import com.lab.service.LabApplyService;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.LabPerm;
import com.lab.web.perm.anno.OwnOrLabPerm;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab-apply")
@Api("申请记录相关请求")
public class LabApplyController {

    @Autowired
    private LabApplyService labApplyService;

    @AddedIn
    @GetMapping("page/my-to-be-confirm/lab-id/{labId}")
    public ResBean<PageBean<LabApplyVO>> pageMyToBeConfirm(@PathVariable Long labId, PageQuery pageQuery) {
        // 查询当前用户的待审批记录（别人向我申请的）
        Long labUserId = LabPermContext.labUserId();
        LabApplyQuery query = LabApplyQuery.builder()
            .fromLabUserId(labUserId)
            .status(LabApplyStatusEnum.TO_BE_CONFIRM.ordinal())
            .build();
        PageBean<LabApplyVO> pageBean = this.labApplyService.pageByQuery(query, pageQuery);
        return ResBean.ok_200(pageBean);
    }

    @AddedIn
    @GetMapping("page/my-lend/lab-id/{labId}")
    public ResBean<PageBean<LabApplyVO>> pageMyLendRecord(@PathVariable Long labId, PageQuery pageQuery) {
        // 查询当前用户的借出记录（别人向我申请的）
        Long labUserId = LabPermContext.labUserId();
        LabApplyQuery query = LabApplyQuery.builder()
            .fromLabUserId(labUserId)
            .build();
        PageBean<LabApplyVO> pageBean = this.labApplyService.pageByQuery(query, pageQuery);
        return ResBean.ok_200(pageBean);
    }

    @AddedIn
    @GetMapping("page/to-be-confirm/lab-id/{labId}")
    public ResBean<PageBean<LabApplyVO>> pageToBeConfirmOfLab(@PathVariable Long labId, PageQuery pageQuery) {
        // 查询实验室待审批的记录（非私人架子）
        LabApplyQuery query = LabApplyQuery.builder()
            .labId(labId)
            .fromLabUserId(0L)
            .status(LabApplyStatusEnum.TO_BE_CONFIRM.ordinal())
            .build();
        PageBean<LabApplyVO> pageBean = this.labApplyService.pageByQuery(query, pageQuery);
        return ResBean.ok_200(pageBean);
    }

    @AddedIn
    @GetMapping("page/my-lab-apply/lab-id/{labId}")
    public ResBean<PageBean<LabApplyVO>> pageMyLabApply(@PathVariable Long labId, PageQuery pageQuery) {
        // 查询当前用户的发起申请记录（不管是否审批）
        Long labUserId = LabPermContext.labUserId();
        LabApplyQuery query = LabApplyQuery.builder()
            .toLabUserId(labUserId)
            .build();
        PageBean<LabApplyVO> pageBean = this.labApplyService.pageByQuery(query, pageQuery);
        return ResBean.ok_200(pageBean);
    }

    @OwnOrLabPerm(StrPerm.RECORD_APPLY)
    @GetMapping("page")
    public ResBean<PageBean<LabApplyVO>> pageByLabId(LabApplyQuery query, PageQuery pageQuery) {
        PageBean<LabApplyVO> pageBean = this.labApplyService.pageByQuery(query, pageQuery);
        return ResBean.ok_200(pageBean);
    }

}
