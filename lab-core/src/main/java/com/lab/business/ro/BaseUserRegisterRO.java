package com.lab.business.ro;

import com.lab.business.constant.BaseUserConstant;
import com.lab.common.exception.ParamErrorException;
import com.lab.business.message.BaseUserMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
public class BaseUserRegisterRO {


    /**
     * 邮箱
     */
    @NotBlank(message = BaseUserMessage.EMAIL_NOT_BLANK)
    @Email(message = BaseUserMessage.EMAIL_INVALID)
    private String email;

    /**
     * 用户名
     */
    @NotBlank(message = BaseUserMessage.USERNAME_NOT_BLANK_MESSAGE)
    @Pattern(regexp = BaseUserConstant.USERNAME_PATTERN, message = BaseUserMessage.USERNAME_INVALID)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = BaseUserMessage.PASSWORD_NOT_BLANK)
    @Pattern(regexp = BaseUserConstant.PASSWORD_PATTERN, message = BaseUserMessage.PASSWORD_INVALID)
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = CONFIRM_PASSWORD_NOT_BLANK)
    private String confirmPassword;

    public static final String CONFIRM_PASSWORD_NOT_BLANK = "确认密码不能为空";

    /**
     * 验证码
     */
    @NotBlank(message = BaseUserMessage.CAPTCHA_NOT_BLANK)
    @Pattern(regexp = BaseUserConstant.CAPTCHA_PATTERN_REGEXP, message = BaseUserMessage.CAPTCHA_INVALID)
    private String captcha;



    /**
     * 补充的参数校验
     */
    public void validatePassword() {
        // 校验两次密码相同
        if (!this.getPassword().equals(this.getConfirmPassword())) {
            throw new ParamErrorException("两次输入的密码不相同");
        }
    }

}
