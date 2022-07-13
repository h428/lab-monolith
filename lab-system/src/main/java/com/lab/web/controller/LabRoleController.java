package com.lab.web.controller;

import com.lab.business.ro.LabRoleCreateRO;
import com.lab.business.ro.LabRoleUpdateRO;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabRoleVO;
import com.lab.cache.LabPermCache;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.service.LabRoleService;
import com.lab.web.perm.PermRecheck;
import com.lab.web.perm.anno.OwnOrLabPerm;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/lab-role")
@Api("实验室角色相关请求")
public class LabRoleController {

    @Autowired
    private LabRoleService labRoleService;

    /**
     * 在修改角色信息后，根据 labRoleId 移除角色缓存，以保持一致性
     * @param labRoleId  角色 id
     */
    private void clearCacheAfterUpdateRole(Long labRoleId) {

        // create 时 labId 为空，无需清除
        if (labRoleId == null) {
            return;
        }

        // 清空缓存
        LabPermCache.removeLabRole(labRoleId);
    }

    @PostMapping
    @OwnOrLabPerm(StrPerm.PERM_ROLE)
    public ResBean<Void> create(@RequestBody LabRoleCreateRO labRoleCreateRO) {

        Long labUserId = LabPermContext.labUserId();
        labRoleCreateRO.setOpLabUserId(labUserId);
        this.labRoleService.create(labRoleCreateRO);

        return ResBean.ok_200();
    }

    @OwnOrLabPerm(StrPerm.PERM_ROLE)
    @DeleteMapping("{id}")
    public ResBean<Void> delete(@PathVariable Long id) {
        // 鉴权重验
        PermRecheck.recheckByDataId(id, this.labRoleService::get);

        this.labRoleService.delete(id);

        clearCacheAfterUpdateRole(id);

        return ResBean.ok_200();
    }

    @PutMapping
    @OwnOrLabPerm(StrPerm.PERM_ROLE)
    public ResBean<Void> update(@RequestBody LabRoleUpdateRO labRoleUpdateRO) {
        PermRecheck.recheckByDataId(labRoleUpdateRO.getId(), this.labRoleService::get);
        Long labUserId = LabPermContext.labUserId();
        labRoleUpdateRO.setOpLabUserId(labUserId);
        this.labRoleService.update(labRoleUpdateRO);

        return ResBean.ok_200();
    }

    @GetMapping("ids")
    public ResBean<List<LabRoleVO>> listByIds(@RequestParam("ids") List<Long> ids) {
        List<LabRoleVO> labRoleVOS = this.labRoleService.listByIds(ids);
        // 鉴权重验
        PermRecheck.recheckLabId(labRoleVOS);
        return ResBean.ok_200(labRoleVOS);
    }

    @GetMapping("lab-id/{labId}")
    public ResBean<List<LabRoleVO>> listByLabId(@PathVariable Long labId) {
        return ResBean.ok_200(labRoleService.listByLabId(labId));
    }

}
