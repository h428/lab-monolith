package com.lab.service;

import com.lab.business.enums.op.OpApplyResultEnum;
import com.lab.business.ro.OpApplyRO;
import com.lab.business.ro.OpConfirmRO;
import com.lab.business.ro.OpInRO;
import com.lab.business.ro.OpMoveRO;
import com.lab.business.ro.OpOutRO;
import com.lab.common.aop.param.ServiceValidated;
import javax.validation.Valid;

public interface OpService {

    @ServiceValidated
    void in(@Valid OpInRO opInRO);

    @ServiceValidated
    void out(@Valid OpOutRO opOutRO);

    @ServiceValidated
    OpApplyResultEnum apply(@Valid OpApplyRO opApplyRO);

    @ServiceValidated
    void move(@Valid OpMoveRO opMoveRO);

    @ServiceValidated
    void confirm(@Valid OpConfirmRO opConfirmRO);
}
