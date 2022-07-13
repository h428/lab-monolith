package com.lab.common.component.email;

import com.aliyun.dm20151123.Client;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.lab.common.dto.EmailDTO;
import com.lab.common.util.PropertyUtil;
import java.util.Properties;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliEmailSender implements EmailSender {

    @Value("${common.ali-email-file}")
    private String aliEmailFileName;

    // 访问密钥 id
    private String accessKeyId;

    // 访问密钥
    private String accessKeySecret;

    // 阿里控制台配置的发信地址，即发件源地址
    private String accountName;

    // 原子模式复用 request 对象
    private final SingleSendMailRequest request = new SingleSendMailRequest();

    @PostConstruct
    public void init() {
        // 根据 yml 文件中配置的文件地址，读取 ali-email.properties 配置
        Properties properties = PropertyUtil.load(aliEmailFileName);
        // 读取 properties 以用于发送邮件
        this.accessKeyId = properties.getProperty("accessKeyId");
        this.accessKeySecret = properties.getProperty("accessKeySecret");
        this.accountName = properties.getProperty("accountName");
        // 构造 client 对象用于发送邮件
        this.client = createClient(accessKeyId, accessKeySecret);
    }

    // 发送邮件的客户端对象
    private Client client;

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     */
    public static Client createClient(String accessKeyId, String accessKeySecret) {
        try {
            Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
            // 访问的域名
            config.endpoint = "dm.aliyuncs.com";
            return new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public boolean sendEmail(EmailDTO emailDTO) {

        // options
        RuntimeOptions runtime = new RuntimeOptions();

        // request 指代一次发信请求
        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setAccountName(accountName) // 设置源地址
            .setAddressType(1) // 1 表示是发信地址
            // 设置目标地址
            // 可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            .setToAddress(emailDTO.getToAddress())
            .setSubject(emailDTO.getSubject())
            //如果采用byte[].toString的方式的话请确保最终转换成utf-8的格式再放入htmlbody和textbody，若编码不一致则会被当成垃圾邮件。
            //注意：文本邮件的大小限制为3M，过大的文本会导致连接超时或413错误
            .setHtmlBody(emailDTO.getHtmlBody())
            .setReplyToAddress(true);

        try {
            // 复制代码运行请自行打印 API 的返回值
            client.singleSendMailWithOptions(request, runtime);
            return true;
        } catch (TeaException error) {
            // 如有需要，请打印 error
            log.info("发送邮件失败，message : " + error.message);
            // com.aliyun.teautil.Common.assertAsString(error.message);
            return false;
        } catch (Exception error) {
            log.info("发送邮件失败，message : " + error.getMessage());
            // TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            // com.aliyun.teautil.Common.assertAsString(error.message);
            return false;
        }
    }

    // 脱离 spring 环境测试
    public static void main(String[] args) {
        AliEmailSender emailSender = new AliEmailSender();

        // 模拟 spring 声明周期
        emailSender.aliEmailFileName = "C:/data/lab/ali-email.properties";
        emailSender.init();

        // 测试发件
        EmailDTO emailDTO = EmailDTO.builder()
            .toAddress("Lyinghao@126.com")
            .subject("注册 labmate 验证码")
            .htmlBody("您正在注册 Labmate，验证码为 333")
            .build();

        System.out.println(emailSender.sendEmail(emailDTO));
    }

}
