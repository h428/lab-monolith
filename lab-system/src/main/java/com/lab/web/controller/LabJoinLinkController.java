package com.lab.web.controller;

import com.lab.business.ro.LabJoinLinkCreateRO;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabJoinLinkVO;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.service.LabJoinLinkService;
import com.lab.web.perm.PermRecheck;
import com.lab.web.perm.anno.OwnOrLabPerm;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab-join-link")
@Api("加入实验室链接相关请求")
public class LabJoinLinkController {

    @Autowired
    private LabJoinLinkService labJoinLinkService;

    @PostMapping
    @OwnOrLabPerm(StrPerm.PERM_ROLE)
    public ResBean<Void> create(@Validated @RequestBody LabJoinLinkCreateRO labJoinLinkCreateRO) {
        labJoinLinkCreateRO.setOpLabUserId(LabPermContext.labUserId());
        labJoinLinkService.create(labJoinLinkCreateRO);
        return ResBean.ok_200();
    }

    @DeleteMapping("{id}")
    @OwnOrLabPerm(StrPerm.PERM_ROLE)
    public ResBean<Void> delete(@PathVariable Long id) {
        PermRecheck.recheckByDataId(id, labJoinLinkService::get);
        labJoinLinkService.deleteById(id);
        return ResBean.ok_200();
    }

    @GetMapping("link/{link}")
    public ResBean<LabJoinLinkVO> getByLink(@PathVariable String link) {
        return ResBean.ok_200(labJoinLinkService.getByLink(link));
    }

    @GetMapping("lab-id/{labId}")
    public ResBean<List<LabJoinLinkVO>> listByLabId(@PathVariable Long labId) {
        return ResBean.ok_200(labJoinLinkService.listByLabId(labId));
    }

}
