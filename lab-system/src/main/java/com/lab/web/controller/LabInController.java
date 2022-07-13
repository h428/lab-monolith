package com.lab.web.controller;

import com.lab.business.query.LabInQuery;
import com.lab.business.vo.LabInVO;
import com.lab.common.bean.PageBean;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.common.query.PageQuery;
import com.lab.service.LabInService;
import com.lab.web.perm.PermRecheck;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.OwnOrLabPerm;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab-in")
@Api("入库记录相关请求")
public class LabInController {

    @Autowired
    private LabInService labInService;

    @GetMapping("ids")
    @AddedIn
    public ResBean<List<LabInVO>> listByIds(@RequestParam("ids") List<Long> ids) {
        List<LabInVO> labInVOS = this.labInService.listByIds(ids);
        PermRecheck.recheckLabId(labInVOS);
        return ResBean.ok_200(labInVOS);
    }

    @OwnOrLabPerm(StrPerm.RECORD_IN)
    @GetMapping("page")
    public ResBean<PageBean<LabInVO>> page(LabInQuery labInQuery, PageQuery pageQuery) {
        PageBean<LabInVO> pageRes = this.labInService.page(labInQuery, pageQuery);
        return ResBean.ok_200(pageRes);
    }

}
