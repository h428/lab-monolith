package com.lab.web.controller;


import cn.hutool.core.lang.Assert;
import com.lab.BaseWebTest;
import com.lab.entity.BaseUser;
import com.lab.common.bean.PageBean;
import com.lab.business.ro.BaseUserLoginRO;
import com.lab.business.vo.LoginResultVo;
import java.util.List;
import org.junit.Test;

public class OpenControllerTest extends BaseWebTest {

    private static final String PREFIX = "/open";

    @Test
    public void login() {

        String url = PREFIX + "/login";

        BaseUserLoginRO body = BaseUserLoginRO.builder()
            .email("lyh@lab.com")
            .password("lyh")
            .build();

        LoginResultVo loginResultVo = post(url).body(body)
            .execute()
            .validateResponseWrapper()
            .getBeanData(LoginResultVo.class);

        Assert.notNull(loginResultVo.getToken());
//        Assert.notNull(loginResultVo.getBaseUser());


    }


    @Test
    public void testList() {

        String url = PREFIX + "/testList";

        List<BaseUser> baseUsers = post(url)
            .execute()
            .validateResponseWrapper()
            .getListData(BaseUser.class);

        System.out.println(baseUsers);


    }

    @Test
    public void test() {

        String url = PREFIX + "/testPage";

        PageBean<BaseUser> baseUsers = post(url)
            .execute()
            .validateResponseWrapper()
            .getPageBeanData(BaseUser.class);

        System.out.println(baseUsers);


    }
}