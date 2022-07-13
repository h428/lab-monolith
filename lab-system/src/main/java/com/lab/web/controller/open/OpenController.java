package com.lab.web.controller.open;

import com.google.common.collect.Lists;
import com.lab.common.component.TokenUtil;
import com.lab.common.util.EntityUtil;
import com.lab.entity.BaseUser;
import com.lab.common.bean.PageBean;
import com.lab.common.bean.ResBean;
import com.lab.business.converter.BaseUserConverter;
import com.lab.business.ro.BaseUserLoginRO;
import com.lab.business.vo.BaseUserVO;
import com.lab.business.vo.LoginResultVo;
import com.lab.service.impl.BaseUserServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open")
public class OpenController {

    @Autowired
    private BaseUserServiceImpl baseUserServiceImpl;

    private final BaseUserConverter baseUserConverter = BaseUserConverter.INSTANCE;


    @PostMapping("login")
    public ResBean<LoginResultVo> login(@RequestBody @Validated BaseUserLoginRO baseUserLoginRO) {
        // todo captcha check
        BaseUserVO baseUser = this.baseUserServiceImpl.loginByEmail(baseUserLoginRO);

        if (baseUser == null) {
            return ResBean.badRequest_400("用户名或密码错误，登录失败");
        }

        String token = TokenUtil.baseUserLogin(baseUser.getId());

        LoginResultVo loginResult = LoginResultVo.builder()
            .token(token)
            .build();

        return ResBean.ok_200(loginResult);
    }


    @GetMapping("testList")
    public ResBean<List<BaseUser>> testList() {

        List<BaseUser> baseUsers = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            BaseUser t = EntityUtil.generateRandomOne(BaseUser.class, i);
            baseUsers.add(t);
        }

        return ResBean.ok_200(baseUsers);
    }

    @GetMapping("testPage")
    public ResBean<PageBean<BaseUser>> testPage() {

        List<BaseUser> baseUsers = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            BaseUser t = EntityUtil.generateRandomOne(BaseUser.class, i);
            baseUsers.add(t);
        }

        PageBean<BaseUser> pageBean = PageBean.<BaseUser>builder().list(baseUsers).build();

        return ResBean.ok_200(pageBean);
    }



}
