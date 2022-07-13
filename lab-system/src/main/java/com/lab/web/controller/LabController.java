package com.lab.web.controller;

import com.lab.business.ro.LabCreateRO;
import com.lab.business.ro.LabUpdateRO;
import com.lab.business.threadlocal.BaseUserIdThreadLocal;
import com.lab.business.vo.LabEntryVO;
import com.lab.business.vo.LabVO;
import com.lab.cache.LabPermCache;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.MyHttpMessage;
import com.lab.common.util.MyAssert;
import com.lab.service.LabService;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.Own;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/lab")
@Api("实验室相关请求")
public class LabController {

    @Autowired
    private LabService labService;

    private void clearCacheAfterCreateLab(Long baseUserId) {
        // 清空缓存
        LabPermCache.removeAddedInLabEntry(baseUserId);
        LabPermCache.removeOwnLabEntry(baseUserId);
    }

    @PostMapping
    public ResBean<Void> create(@RequestBody LabCreateRO labCreateRO) {
        Long baseUserId = BaseUserIdThreadLocal.get();
        labCreateRO.setCreateBaseUserId(baseUserId);
        this.labService.create(labCreateRO);

        // 清空缓存
        clearCacheAfterCreateLab(baseUserId);

        return ResBean.ok_200();
    }

    @DeleteMapping("{labId}")
    @Own
    public ResBean<Void> delete(@PathVariable Long labId) {
        this.labService.fakeDeleteById(labId);
        return ResBean.ok_200();
    }

    @PutMapping
    @Own
    public ResBean<Void> update(@RequestBody @Validated LabUpdateRO labUpdateRO) {
        this.labService.update(labUpdateRO);
        return ResBean.ok_200();
    }

    @GetMapping("{labId}")
    @AddedIn
    public ResBean<LabVO> getById(@PathVariable Long labId) {
        return ResBean.ok_200(this.labService.get(labId));
    }

    @GetMapping("ids")
    public ResBean<List<LabVO>> listByIds(@RequestParam("ids") List<Long> ids) {
        Long baseUserId = BaseUserIdThreadLocal.get();
        List<LabVO> labVOS = this.labService.listByIds(ids);
        labVOS.forEach(labVO ->
            MyAssert.isTrue(LabPermCache.addedIn(baseUserId, labVO.getId()), MyHttpMessage.FORBIDDEN));
        return ResBean.ok_200(labVOS);
    }

    @GetMapping("own-lab-entry-map")
    public ResBean<Map<Long, LabEntryVO>> ownLabEntryMap() {
        Long baseUserId = BaseUserIdThreadLocal.get();
        return ResBean.ok_200(this.labService.findOwnLabEntryMap(baseUserId));
    }

    @GetMapping("added-in-lab-entry-map")
    public ResBean<Map<Long, LabEntryVO>> addedInLabEntryMap() {
        Long baseUserId = BaseUserIdThreadLocal.get();
        return ResBean.ok_200(this.labService.findAddedInLabEntryMap(baseUserId));
    }

}
