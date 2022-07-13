package com.lab.web.controller.open;

import com.lab.business.message.BaseUserMessage;
import com.lab.common.bean.ResBean;
import com.lab.service.BaseUserService;
import javax.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open/check")
@Validated
public class CheckController {

    @Autowired
    private BaseUserService baseUserService;

    @GetMapping("email-exist/{email}")
    public ResBean<Boolean> existByEmail(@PathVariable @Email(message = BaseUserMessage.EMAIL_MESSAGE) String email) {
        return ResBean.ok_200(this.baseUserService.existByEmail(email));
    }

    @GetMapping("username-exist/{username}")
    public ResBean<Boolean> existByUsername(@PathVariable String username) {
        return ResBean.ok_200(this.baseUserService.existByUsername(username));
    }


}
