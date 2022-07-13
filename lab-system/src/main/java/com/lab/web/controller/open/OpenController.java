package com.lab.web.controller.open;

import com.lab.business.converter.BaseUserConverter;
import com.lab.business.message.BaseUserMessage;
import com.lab.business.ro.BaseUserLoginRO;
import com.lab.business.ro.BaseUserRegisterRO;
import com.lab.business.ro.BaseUserResetPasswordRO;
import com.lab.business.vo.BaseUserVO;
import com.lab.business.vo.LoginResultVo;
import com.lab.common.bean.ResBean;
import com.lab.common.component.RedisUtil;
import com.lab.common.component.TokenUtil;
import com.lab.common.exception.ParamErrorException;
import com.lab.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open")
public class OpenController {

    @Autowired
    private BaseUserService baseUserService;

    private final BaseUserConverter baseUserConverter = BaseUserConverter.INSTANCE;


    @PostMapping("login")
    public ResBean<LoginResultVo> login(@RequestBody @Validated BaseUserLoginRO baseUserLoginRO) {
        // todo captcha check
        BaseUserVO baseUser = this.baseUserService.loginByEmail(baseUserLoginRO);

        if (baseUser == null) {
            return ResBean.badRequest_400("用户名或密码错误，登录失败");
        }

        String token = TokenUtil.baseUserLogin(baseUser.getId());

        LoginResultVo loginResult = LoginResultVo.builder()
            .token(token)
            .build();

        return ResBean.ok_200(loginResult);
    }


    @PostMapping("register")
    public ResBean<Void> register(@Validated @RequestBody BaseUserRegisterRO baseUserRegisterRO) {

        // 业务校验参数
        this.checkRegisterParam(baseUserRegisterRO);

        this.baseUserService.register(baseUserRegisterRO);

        return ResBean.ok_200();
    }

    // 注册参数校验：涉及业务
    private void checkRegisterParam(BaseUserRegisterRO baseUserRegisterRO) {

        baseUserRegisterRO.validatePassword();

        String email = baseUserRegisterRO.getEmail();

        // 校验验证码是否合法
        String key = CaptchaController.REGISTER_CAPTCHA_PREFIX + email;
        if (!baseUserRegisterRO.getCaptcha().equals(RedisUtil.get(key))) {
            throw new ParamErrorException(BaseUserMessage.CAPTCHA_ERROR);
        }

        // 校验邮箱是否已注册
        if (this.baseUserService.existByEmail(email)) {
            throw new ParamErrorException(BaseUserMessage.EMAIL_USED);
        }

        // 校验用户名是否已存在
        String username = baseUserRegisterRO.getUsername();
        if (this.baseUserService.existByUsername(username)) {
            throw new ParamErrorException(BaseUserMessage.USERNAME_USED);
        }
    }

    @PostMapping("reset-password")
    public ResBean<Void> resetPassword(@RequestBody @Validated BaseUserResetPasswordRO baseUserResetPasswordRO) {

        // 补充的校验
        baseUserResetPasswordRO.validate();

        // 校验验证码是否合法
        String captchaKey = CaptchaController.RESET_PASSWORD_CAPTCHA_PREFIX + baseUserResetPasswordRO.getEmail();
        String captcha = RedisUtil.get(captchaKey);

        if (!baseUserResetPasswordRO.getCaptcha().equals(captcha)) {
            throw new ParamErrorException(BaseUserMessage.CAPTCHA_ERROR);
        }

        // 重置密码
        this.baseUserService.resetPassword(baseUserResetPasswordRO);

        // 移除验证码
        RedisUtil.remove(captchaKey);

        return ResBean.ok_200();
    }

}
