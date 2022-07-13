package com.lab.web.controller;

import com.lab.business.converter.LabOutConverter;
import com.lab.business.query.LabOutQuery;
import com.lab.business.query.MyLabOutQuery;
import com.lab.business.threadlocal.LabPermContext;
import com.lab.business.vo.LabOutVO;
import com.lab.common.bean.PageBean;
import com.lab.common.bean.ResBean;
import com.lab.common.constant.StrPerm;
import com.lab.common.query.PageQuery;
import com.lab.service.LabOutService;
import com.lab.web.perm.anno.AddedIn;
import com.lab.web.perm.anno.OwnOrLabPerm;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab-out")
@Api("消耗记录相关请求")
public class LabOutController {

    @Autowired
    private LabOutService labOutService;

    private final LabOutConverter labOutConverter = LabOutConverter.INSTANCE;

    @AddedIn
    @GetMapping("page/my-lab-out")
    public ResBean<PageBean<LabOutVO>> pageMyLabOut(MyLabOutQuery myLabOutQuery, PageQuery pageQuery) {
        // labUserId 是正确的，且必定有该过滤条件，因此无需权限重验也不会查到不该查到的数据
        Long labUserId = LabPermContext.labUserId();
        LabOutQuery labOutQuery = labOutConverter.myOutQueryToLabOutQuery(myLabOutQuery);
        labOutQuery.setOpLabUserId(labUserId);
        return ResBean.ok_200(this.labOutService.pageByQuery(labOutQuery, pageQuery));
    }

    @OwnOrLabPerm(StrPerm.RECORD_OUT)
    @GetMapping("page")
    public ResBean<PageBean<LabOutVO>> page(LabOutQuery labOutQuery, PageQuery pageQuery) {
        // 内部再查询时需要补充 labId 作为过滤条件，就可以不用权限重验
        // 如果把 labId 置空了，则需要权限重验
        return ResBean.ok_200(this.labOutService.pageByQuery(labOutQuery, pageQuery));
    }

}
