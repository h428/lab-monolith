package com.lab.web.controller;

import com.lab.business.ro.LabItemCreateRO;
import com.lab.business.ro.LabItemUpdateRO;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabItemVO;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.service.LabItemService;
import com.lab.web.perm.PermRecheck;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.LabPerm;
import com.lab.web.perm.anno.OwnOrAddedIn;
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
@RequestMapping("/lab-item")
@Api("实验室物品相关请求")
public class LabItemController {

    @Autowired
    private LabItemService labItemService;


    @LabPerm(StrPerm.LAB_ITEM)
    @PostMapping
    public ResBean<Void> create(@Validated @RequestBody LabItemCreateRO labItemCreateRO) {

        // 补充信息
        labItemCreateRO.setOpLabUserId(LabPermContext.labUserId());

        this.labItemService.create(labItemCreateRO);
        return ResBean.ok_200();
    }

    // 查询参数 labId 用于辅助鉴权
    @LabPerm(StrPerm.LAB_ITEM)
    @DeleteMapping("{id}")
    public ResBean<Void> delete(@PathVariable Long id) {
        // 预查询数据做鉴权重验
        PermRecheck.recheckByDataId(id, labItemService::get);

        this.labItemService.delete(id);

        return ResBean.ok_200();
    }

    @LabPerm(StrPerm.LAB_ITEM)
    @PutMapping
    public ResBean<Void> update(@Validated @RequestBody LabItemUpdateRO labItemUpdateRO) {
        // 权限重验
        PermRecheck.recheckByDataId(labItemUpdateRO.getId(), labItemService::get);

        // 补充信息
        labItemUpdateRO.setOpLabUserId(LabPermContext.labUserId());

        this.labItemService.update(labItemUpdateRO);
        return ResBean.ok_200();
    }

    @GetMapping("ids")
    @AddedIn
    public ResBean<List<LabItemVO>> listByIds(@RequestParam("ids") List<Long> ids) {
        List<LabItemVO> labItemVOS = this.labItemService.listByIds(ids);
        PermRecheck.recheckLabId(labItemVOS);
        return ResBean.ok_200(labItemVOS);
    }

    @GetMapping("lab-id/{labId}")
    @OwnOrAddedIn
    public ResBean<List<LabItemVO>> listByLabId(@PathVariable Long labId) {
        return ResBean.ok_200(this.labItemService.listByLabId(labId));
    }

}
