package com.lab.business.ro;

import com.lab.business.message.BaseUserMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class BaseUserLoginRO {

    /**
     * 电子邮箱
     */
    @NotBlank(message = BaseUserMessage.EMAIL_NOT_BLANK)
    @Email(message = BaseUserMessage.EMAIL_INVALID)
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = BaseUserMessage.PASSWORD_NOT_BLANK)
    private String password;

    /**
     * 验证码
     */
    private String captcha; // 暂时不添加校验

}
