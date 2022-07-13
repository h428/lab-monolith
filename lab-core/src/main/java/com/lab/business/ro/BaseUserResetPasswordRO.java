package com.lab.business.ro;

import com.lab.business.constant.BaseUserConstant;
import com.lab.business.message.BaseUserMessage;
import com.lab.common.exception.ParamErrorException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用于重置密码
 *
 * @author hao
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BaseUserResetPasswordRO {

    /**
     * 邮箱
     */
    @NotBlank(message = BaseUserMessage.EMAIL_NOT_BLANK)
    @Email(message = BaseUserMessage.EMAIL_INVALID)
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = BaseUserMessage.PASSWORD_NOT_BLANK)
    @Pattern(regexp = BaseUserConstant.PASSWORD_PATTERN, message = BaseUserMessage.PASSWORD_INVALID)
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = BaseUserMessage.CONFIRM_PASSWORD_NOT_BLANK)
    private String confirmPassword;

    /**
     * 验证码
     */
    @NotBlank(message = BaseUserMessage.CAPTCHA_NOT_BLANK)
    @Pattern(regexp = BaseUserConstant.CAPTCHA_PATTERN_REGEXP,
            message = BaseUserMessage.CAPTCHA_INVALID)
    private String captcha;

    /**
     * 补充的参数校验
     */
    public void validate() {
        // 校验两次密码相同
        if (!this.getPassword().equals(this.getConfirmPassword())) {
            throw new ParamErrorException("两次输入的密码不相同");
        }
    }

}
