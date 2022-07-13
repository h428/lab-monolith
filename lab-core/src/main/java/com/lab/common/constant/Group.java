package com.lab.common.constant;

/**
 * 校验组标记；
 *
 * 前置说明：个人习惯将 Controller 的入参记为 XxxRO，出参记为 XxxVO；Service 的入参和出参都记为 XxxDTO；理论上，对于
 * Controller 层和 Service 层有不同的入参，最好分别创建 XxxRO 和 XxxDTO 作为入参，创建 XxxVO 和 XxxDTO 作为出参，
 * 这样做的好处是职责单一，添加字段也较为方便；但这样会导致代码冗余的问题，而且大多数情况下，同一调用链上的 DTO 和 RO 相比，
 * 入参字段基本一致，DTO 一般就比 RO 多了一个 userId，其为在 Controller 层根据当前用户登录用户获取到的用户 id，
 * 以标识本次调用的归属用户；而对于出参，若没有复杂的业务解释，简单的系统大多数情况下可以省略 VO，直接对 Service 层输出的
 * DTO 中添加部分业务解释字段即可（例如对于性别字段的 0/1 在有古风需求时转化为"公子"就属于业务解释）
 *
 * 对于本项目，出于减少代码冗余的考量，打算将入参的 RO 和 DTO 统一整合为 RO，将出参的 VO 和 DTO 统一整合为 VO；
 * 但整合起来后，在不同阶段有不同的校验，例如对于 userId，在 Controller 层的入参肯定是没有的，其要到 Controller 层才能
 * 获取到当前已登录用户的 userId 并往下传递，故校验是在 Service 层的入参做，因此引入 Group 标记类，标记字段校验在哪个阶段做；
 * 若是没有标记，则相当于两个阶段都做（ps：校验会做两次）
 *
 * 采用这种方案，本项目将不再有 DTO，只有作为入参的 RO 和作为出参的 VO，而入参的类命名往往带有动作信息，例如 UserRegisterRO，
 * LabCreateRO，而出参一般为纯名词表示信息如 UserVo, LabVO
 */
public interface Group {

    interface Web {

    }

    interface Service {

    }

}
