package com.lab.business.ro;

import cn.hutool.core.util.StrUtil;
import com.lab.common.exception.ParamErrorException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用于更新密码
 *
 * @author hao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class BaseUserUpdatePasswordRO {

    private Long id;

    private String oldPassword;

    private String password;

    private String confirmPassword;

    public void checkPasswordBusinessValid() {
        if (StrUtil.equals(oldPassword, password)) {
            throw new ParamErrorException("旧密码不能和新密码一致");
        }

        if (!StrUtil.equals(password, confirmPassword)) {
            throw new ParamErrorException("两次新密码输入不一致");
        }
    }



}
