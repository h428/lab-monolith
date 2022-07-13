package com.lab.common.component.email;

import com.lab.common.dto.EmailDTO;

/**
 * 发送邮件接口，策略模式，可能有不同的云服务接口，不同的账号
 *
 * @author hao
 */
public interface EmailSender {
    boolean sendEmail(EmailDTO emailDTO);
}
