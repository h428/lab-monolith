package com.lab.web.controller;

import com.lab.business.threadlocal.BaseUserIdThreadLocal;
import com.lab.business.vo.BaseUserVO;
import com.lab.business.vo.LabRoleVO;
import com.lab.common.bean.ResBean;
import com.lab.common.component.TokenUtil;
import com.lab.common.constant.MyHttpHeader;
import com.lab.service.BaseUserService;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base-user")
@Api("基础用户相关请求")
public class BaseUserController {

    @PostMapping("logout")
    public ResBean<Void> logout(@RequestHeader(MyHttpHeader.AUTHORIZATION) String token) {
        TokenUtil.baseUserLogout(token);
        return ResBean.ok_200("退出成功");
    }

    @Autowired
    private BaseUserService baseUserService;

    @GetMapping("current")
    public ResBean<BaseUserVO> current() {
        Long baseUserId = BaseUserIdThreadLocal.get();
        return ResBean.ok_200(baseUserService.get(baseUserId));
    }

    @GetMapping("ids")
    public ResBean<List<BaseUserVO>> listByIds(@RequestParam("ids") List<Long> ids) {
        return ResBean.ok_200(this.baseUserService.listByIds(ids));
    }

}
