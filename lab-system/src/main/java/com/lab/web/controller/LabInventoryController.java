package com.lab.web.controller;

import com.lab.business.query.LabInventoryQuery;
import com.lab.business.vo.LabInventoryVO;
import com.lab.business.vo.LabRoleVO;
import com.lab.common.bean.PageBean;
import com.lab.common.bean.ResBean;
import com.lab.common.query.PageQuery;
import com.lab.entity.LabInventory;
import com.lab.service.LabInventoryService;
import com.lab.service.LabRoleService;
import com.lab.web.perm.anno.AddedIn;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab-inventory")
@Api("实验室库存相关请求")
public class LabInventoryController {

    @Autowired
    private LabInventoryService labInventoryService;

    @GetMapping("page")
    @AddedIn
    public ResBean<PageBean<LabInventoryVO>> page(@Validated LabInventoryQuery labInventoryQuery, PageQuery pageQuery) {
        return ResBean.ok_200(this.labInventoryService.page(labInventoryQuery, pageQuery));
    }

}
