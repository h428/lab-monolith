package com.lab.web.controller;

import com.lab.business.converter.LabShelfConverter;
import com.lab.business.enums.col.LabShelfTypeEnum;
import com.lab.business.ro.LabShelfCreateRO;
import com.lab.business.ro.LabShelfUpdateRO;
import com.lab.business.ro.MyLabShelfCreateRO;
import com.lab.business.threadlocal.BaseUserIdThreadLocal;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabEntryVO;
import com.lab.business.vo.LabShelfVO;
import com.lab.cache.LabPermCache;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.common.util.MyAssert;
import com.lab.service.LabShelfService;
import com.lab.web.perm.PermRecheck;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.LabPerm;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab-shelf")
@Api("实验室架子相关请求")
public class LabShelfController {

    @Autowired
    private LabShelfService labShelfService;

    private final LabShelfConverter labShelfConverter = LabShelfConverter.INSTANCE;

    @LabPerm(StrPerm.LAB_SHELF)
    @PostMapping
    public ResBean<List<LabShelfVO>> create(@Validated @RequestBody LabShelfCreateRO labShelfCreateRO) {

        // 额外的入参校验
        labShelfCreateRO.validate();

        // 补充额外参数
        Long labUserId = LabPermContext.labUserId();
        labShelfCreateRO.setOpLabUserId(labUserId);

        // 创建架子
        this.labShelfService.create(labShelfCreateRO);
        return ResBean.ok_200();
    }

    @AddedIn
    @PostMapping("my")
    public ResBean<List<LabShelfVO>> createMy(@Validated @RequestBody MyLabShelfCreateRO myLabShelfCreateRO) {

        // 转化为普通的 labShelfCreateRo 以复用创建方法
        LabShelfCreateRO labShelfCreateRO = this.labShelfConverter.convert(myLabShelfCreateRO);

        Long labUserId = LabPermContext.labUserId();

        // 补充创建个人架子的必须数据
        labShelfCreateRO.setType(LabShelfTypeEnum.PRIVATE.ordinal());
        labShelfCreateRO.setBelongLabUserId(labUserId);
        labShelfCreateRO.setOpLabUserId(labUserId);

        // 执行创建个人架子
        this.labShelfService.create(labShelfCreateRO);

        return ResBean.ok_200();
    }

    @LabPerm(StrPerm.LAB_SHELF)
    @DeleteMapping("{id}")
    public ResBean<Void> delete(@PathVariable Long id) {

        PermRecheck.recheckByDataId(id, labShelfService::get);

        this.labShelfService.delete(id);

        return ResBean.ok_200();
    }

    @LabPerm(StrPerm.LAB_SHELF)
    @PutMapping
    public ResBean<List<LabShelfVO>> update(@Validated @RequestBody LabShelfUpdateRO labShelfUpdateRO) {

        // 权限重验
        PermRecheck.recheckByDataId(labShelfUpdateRO.getId(), this.labShelfService::get);

        // 补充必备数据
        labShelfUpdateRO.setOpLabUserId(LabPermContext.labUserId());

        // 更新
        this.labShelfService.update(labShelfUpdateRO);

        return ResBean.ok_200();
    }

    @AddedIn
    @PutMapping("my")
    public ResBean<List<LabShelfVO>> updateMy(@Validated @RequestBody LabShelfUpdateRO labShelfUpdateRO) {

        // 权限重验
        LabShelfVO labShelfVO = PermRecheck.recheckByDataId(labShelfUpdateRO.getId(), this.labShelfService::get);

        Long labUserId = LabPermContext.labUserId();

        // 权限重验2：业务级权限重验，确保更新的架子是属于当前登录用户的个人架子
        MyAssert.equals(labUserId, labShelfVO.getBelongLabUserId(), "参数非法");

        // 补充必备数据
        labShelfUpdateRO.setOpLabUserId(labUserId);

        // 更新
        this.labShelfService.update(labShelfUpdateRO);

        return ResBean.ok_200();
    }

    @GetMapping("ids")
    @AddedIn
    public ResBean<List<LabShelfVO>> listByIds(@RequestParam("ids") List<Long> ids) {
        List<LabShelfVO> labShelfVOS = this.labShelfService.listByIds(ids);
        PermRecheck.recheckLabId(labShelfVOS);
        return ResBean.ok_200(labShelfVOS);
    }


    @AddedIn
    @GetMapping("my/lab-id/{labId}")
    public ResBean<List<LabShelfVO>> myLabShelfListByLabId(@PathVariable Long labId) {

        Long baseUserId = BaseUserIdThreadLocal.get();

        LabEntryVO addedInLabEntry = LabPermCache.getAddedInLabEntry(baseUserId, labId);
        Long labUserId = addedInLabEntry.getLabUserId();

        return ResBean.ok_200(this.labShelfService.listByLabUserId(labUserId));
    }

    @AddedIn
    @GetMapping("lab-id/{labId}")
    public ResBean<List<LabShelfVO>> listByLabId(@PathVariable Long labId) {
        return ResBean.ok_200(this.labShelfService.listByLabId(labId));
    }

}
