package com.lab.web.controller;

import com.lab.business.ro.CurrentLabUserUpdateRO;
import com.lab.business.ro.JoinRO;
import com.lab.business.ro.LabUserUpdateRO;
import com.lab.business.threadlocal.BaseUserIdThreadLocal;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabUserVO;
import com.lab.cache.LabPermCache;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.service.LabUserService;
import com.lab.web.perm.PermRecheck;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.OwnOrLabPerm;
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
@RequestMapping("/lab-user")
@Api("实验室用户相关请求")
public class LabUserController {

    @Autowired
    private LabUserService labUserService;

    private void clearCacheAfterUpdateLabUser(Long baseUserId) {
        // 清空缓存
        LabPermCache.removeAddedInLabEntry(baseUserId);
    }

    @PostMapping("join")
    public ResBean<Void> joinByLink(@Validated @RequestBody JoinRO joinRO) {
        // 补充入参
        Long baseUserId = BaseUserIdThreadLocal.get();
        joinRO.setBaseUserId(baseUserId);

        // 执行加入实验室逻辑
        this.labUserService.joinByLink(joinRO);

        // 根据 baseUserId 清除缓存
        clearCacheAfterUpdateLabUser(baseUserId);

        return ResBean.ok_200();
    }

    @DeleteMapping("{id}")
    @OwnOrLabPerm(StrPerm.PERM_USER)
    public ResBean<Void> delete(@PathVariable Long id) {

        // 权限重验
        LabUserVO labUserVO = PermRecheck.recheckByDataId(id, this.labUserService::get);

        // 执行伪删除
        this.labUserService.fakeDeleteById(id);

        // 注意是清除被删除用户的 baseUserId 的缓存，不是当前用户
        clearCacheAfterUpdateLabUser(labUserVO.getBaseUserId());

        return ResBean.ok_200();
    }

    /**
     * 用户退出实验室
     * @param labId
     * @return
     */
    @PostMapping("leave/{labId}")
    @AddedIn
    public ResBean<Void> leave(@PathVariable Long labId) {

        Long labUserId = LabPermContext.labUserId();

        // 执行退出实验室操作
        this.labUserService.leave(labUserId);

        // 清除当前 baseUserId 的缓存
        clearCacheAfterUpdateLabUser(BaseUserIdThreadLocal.get());

        return ResBean.ok_200();
    }

    @AddedIn
    @PutMapping("current")
    public ResBean<Void> updateCurrentLabUser(@Validated @RequestBody CurrentLabUserUpdateRO currentLabUserUpdateRO) {

        // 补充入参
        Long labUserId = LabPermContext.labUserId();
        LabUserUpdateRO updateRO = LabUserUpdateRO.builder()
            .id(labUserId)
            .name(currentLabUserUpdateRO.getName())
            .build();

        // 执行更新操作
        this.labUserService.update(updateRO);

        // 更新完毕后根据 baseUserId 移除缓存
        Long baseUserId = BaseUserIdThreadLocal.get();
        clearCacheAfterUpdateLabUser(baseUserId);

        return ResBean.ok_200();
    }

    @OwnOrLabPerm(StrPerm.PERM_USER)
    @PutMapping
    public ResBean<Void> update(@Validated @RequestBody LabUserUpdateRO labUserUpdateRO) {
        // 权限重验
        LabUserVO labUserVO = PermRecheck.recheckByDataId(
            labUserUpdateRO.getId(), this.labUserService::get);

        // 入参补充
        labUserUpdateRO.setOpLabUserId(LabPermContext.labUserId());

        // 更新操作
        this.labUserService.update(labUserUpdateRO);

        // 根据 baseUserId 移除缓存
        // 注意是清除被删除用户的 baseUserId 的缓存，不是当前用户
        clearCacheAfterUpdateLabUser(labUserVO.getBaseUserId());

        return ResBean.ok_200();
    }

    @GetMapping("current/lab-id/{labId}")
    public ResBean<LabUserVO> currentLabUser(@PathVariable Long labId) {
        Long baseUserId = BaseUserIdThreadLocal.get();
        LabUserVO addedInVo = labUserService.findAddedInVo(baseUserId, labId);
        return ResBean.ok_200(addedInVo);
    }

    @GetMapping("ids")
    public ResBean<List<LabUserVO>> listByIds(@RequestParam("ids") List<Long> ids) {
        List<LabUserVO> labUserVOS = this.labUserService.listByIds(ids);
        PermRecheck.recheckLabId(labUserVOS);
        return ResBean.ok_200(labUserVOS);
    }

    @GetMapping("lab-id/{labId}")
    public ResBean<List<LabUserVO>> listByLabId(@PathVariable Long labId) {
        return ResBean.ok_200(this.labUserService.listByLabId(labId));
    }

}
