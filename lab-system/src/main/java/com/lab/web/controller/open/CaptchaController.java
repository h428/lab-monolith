package com.lab.web.controller.open;

import com.lab.business.message.BaseUserMessage;
import com.lab.common.bean.ResBean;
import com.lab.service.BaseUserService;
import com.lab.web.component.CaptchaComponent;
import javax.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open/captcha")
@Validated
public class CaptchaController {

    public static String REGISTER_CAPTCHA_PREFIX = "REGISTER_CAPTCHA:";

    @Autowired
    private CaptchaComponent captchaComponent;

    @Autowired
    private BaseUserService baseUserService;


    @PostMapping("register/{email}")
    public ResBean<Void> registerCaptcha(
        @PathVariable @Email(message = "邮箱格式不正确") String email) {

        if (this.baseUserService.existByEmail(email)) {
            // 邮箱已注册
            return ResBean.badRequest_400(BaseUserMessage.EMAIL_USED);
        }

        // 本次发送验证码的邮件主题以及对应的操作内容
        final String subject = "Labmate 注册";
        final String op = "注册 Labmate";

        return this.captchaComponent.captchaTemplate(REGISTER_CAPTCHA_PREFIX, email, subject, op);
    }

    public static final String RESET_PASSWORD_CAPTCHA_PREFIX = "RESET_PASSWORD_CAPTCHA:";

    @PostMapping("reset-password/{email}")
    public ResBean<Void> resetPassword(
        @PathVariable @Email(message = "邮箱格式不正确") String email) {

        if (!baseUserService.existByEmail(email)) {
            return ResBean.badRequest_400(BaseUserMessage.EMAIL_UNUSED);
        }

        // 本次发送验证码的邮件主题以及对应的操作内容
        final String subject = "Labmate 重置密码";
        final String op = "重置用户密码";

        return this.captchaComponent.captchaTemplate(RESET_PASSWORD_CAPTCHA_PREFIX, email, subject, op);
    }

}
