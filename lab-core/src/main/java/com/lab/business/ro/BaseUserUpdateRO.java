package com.lab.business.ro;

import com.lab.business.message.BaseUserMessage;
import com.lab.common.constant.CommonRegexpPattern;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用于更新 BaseUser
 *
 * @author hao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class BaseUserUpdateRO {

    private Long id;

    /**
     * 用户名
     */
    @Pattern(regexp = CommonRegexpPattern.USERNAME, message = BaseUserMessage.USERNAME_PATTERN_MESSAGE)
    private String username;

    private String avatar;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;


    private Boolean allowAdd;

}
