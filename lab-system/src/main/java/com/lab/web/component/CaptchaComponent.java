package com.lab.web.component;

import com.lab.business.message.BaseUserMessage;
import com.lab.common.bean.ResBean;
import com.lab.common.component.RedisUtil;
import com.lab.common.component.email.EmailSender;
import com.lab.common.ro.EmailRO;
import com.lab.common.exception.BusinessException;
import com.lab.common.exception.ParamErrorException;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 复用的发送验证码的组件
 *
 * @author hao
 */
@Component
public class CaptchaComponent {

    /**
     * 真正发送邮件的微服务
     */
    @Autowired
    private EmailSender emailSender;

    private static final long EXPIRE_SECONDS = 300L; // 验证码缓存 5 分钟

    /**
     * 发送验证码的模板方法
     *
     * @param prefix 验证码存储在 redis 中的前缀
     * @param email 发送地址
     * @param subject 邮件主题
     * @param op 操作内容（出现在邮件中）
     * @return ResBean
     */
    public ResBean<Void> captchaTemplate(final String prefix, String email,
        String subject, String op) {
        // 若邮箱已经有对应验证码，则不允许再次发送，需要等过期，过期时间五分钟
        if (RedisUtil.exist(prefix + email)) {
            throw new ParamErrorException(BaseUserMessage.SEND_EMAIL_TOO_FREQUENT);
        }

        // 没有验证码，则生成验证码（6 个数字），并存储到 redis 中
        String checkCode = String.valueOf(RandomUtils.nextInt(100000, 1000000));

        // 邮件内容，变动的信息为验证码和 op 信息
        final String htmlBody = "尊敬的用户：<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好，您正在"
            + op + "，验证码为 " + checkCode
            + "，该验证码 5 分钟内有效。（该邮件为系统邮件，请不要回复该邮件）";

        // 发送邮件
        EmailRO emailRO = EmailRO.builder()
            .subject(subject)
            .toAddress(email)
            .htmlBody(htmlBody)
            .build();

        if (!this.emailSender.sendEmail(emailRO)) {
            throw new BusinessException(op + "验证码发送失败，请稍后重试");
        }

        RedisUtil.put(prefix + email, checkCode, EXPIRE_SECONDS);
        return ResBean.ok_200(op + " 验证码发送成功");
    }

}
