package com.lab.service;

import cn.hutool.core.util.RandomUtil;
import com.lab.BaseTest;
import com.lab.business.ro.BaseUserLoginRO;
import com.lab.business.ro.BaseUserRegisterRO;
import com.lab.business.ro.BaseUserResetPasswordRO;
import com.lab.business.ro.BaseUserUpdatePasswordRO;
import com.lab.business.ro.BaseUserUpdateRO;
import com.lab.service.impl.BaseUserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseUserServiceImplTest extends BaseTest {

    @Autowired
    private BaseUserServiceImpl baseUserServiceImpl;

    @Test
    public void register() {

        BaseUserRegisterRO registerDTO = BaseUserRegisterRO.builder()
            .email("Lyinghao2@126.com")
            .username("lyh222")
            .password("aa123456")
            .confirmPassword("aa123456")
            .build();
        baseUserServiceImpl.register(registerDTO);
    }

    @Test
    public void loginByEmail() {

        BaseUserLoginRO userLoginDto = BaseUserLoginRO.builder()
            .email("Lyinghao@126.com")
            .password("aa123456")
            .build();

        Assert.assertNull(baseUserServiceImpl.loginByEmail(userLoginDto));

        BaseUserLoginRO lyh = BaseUserLoginRO.builder()
            .email("lyh@lab.com")
            .password("lyh")
            .build();

        Assert.assertNotNull(baseUserServiceImpl.loginByEmail(lyh));

    }

    @Test
    public void update() {
        BaseUserUpdateRO updateDto = BaseUserUpdateRO.builder()
            .id(1L)
            .username("ttt")
            .name("hao22")
            .avatar("https://123.com")
            .build();

        baseUserServiceImpl.update(updateDto);

    }

    @Test
    public void updatePassword() {
        String password = "lyh" + RandomUtil.randomInt(1000);
        BaseUserUpdatePasswordRO passwordDto = BaseUserUpdatePasswordRO.builder()
            .id(1L)
            .oldPassword("lyh")
            .password(password)
            .confirmPassword(password)
            .build();

        this.baseUserServiceImpl.updatePassword(passwordDto);

        BaseUserLoginRO loginDto = BaseUserLoginRO.builder()
            .email("lyh@lab.com")
            .password(password)
            .build();

        Assert.assertNotNull(this.baseUserServiceImpl.loginByEmail(loginDto));

    }

    @Test
    public void resetPassword() {
        String password = "lyh" + RandomUtil.randomInt(1000);
        BaseUserResetPasswordRO passwordDto = BaseUserResetPasswordRO.builder()
            .email("lyh@lab.com")
            .password(password)
            .confirmPassword(password)
            .build();

        this.baseUserServiceImpl.resetPassword(passwordDto);

        BaseUserLoginRO loginDto = BaseUserLoginRO.builder()
            .email("lyh@lab.com")
            .password(password)
            .build();

        Assert.assertNotNull(this.baseUserServiceImpl.loginByEmail(loginDto));
    }
}