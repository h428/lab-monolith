package com.lab.common.ro;

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
public class EmailRO {

    /**
     * 目标邮箱地址
     */
    @Email(message = "邮箱地址格式不正确")
    private String toAddress;

    /**
     * 邮件主题，即邮件标题
     */
    @NotBlank(message = "邮件主题不能为空")
    private String subject;

    /**
     * 邮件正文
     */
    @NotBlank(message = "邮件内容不能为空")
    private String htmlBody;

}
