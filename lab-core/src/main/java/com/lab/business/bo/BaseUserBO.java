package com.lab.business.bo;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.lab.common.util.CodecUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class BaseUserBO {

    private String password;
    private String salt;


    /**
     * 生成 salt ，以及根据用户输入的原有密码生成加盐加密后的密码；
     * 前置条件：password 已经设置且不为空
     */
    public void generateSaltAndPassword() {
        Assert.notEmpty(this.password);

        // 生成并设置盐值
        String salt = SecureUtil.sha256(IdUtil.fastSimpleUUID());
        this.setSalt(salt);
        // 对原密码加盐 hash 后的到新密码
        String originPassword = this.getPassword(); // 未加密的密码
        // 新密码加密为采用：sha2(原始密码 + salt)
        this.setPassword(SecureUtil.sha256(originPassword + salt));
    }

    /**
     * 用已有的 salt 和 password 校验输入的密码是否正确
     * @param originPassword
     * @return
     */
    public boolean passwordValid(String originPassword) {

        if (this.salt == null || this.password == null) {
            throw new IllegalStateException("salt 和 password 为空，无法校验输入密码的正确性");
        }

        String hashPass = CodecUtil.sha256Hex(originPassword, this.getSalt());

        // 加密后的密码和原来保存的加密结果不相同，则 false
        if (!hashPass.equals(this.getPassword())) {
            return false;
        }

        return true;
    }

}
