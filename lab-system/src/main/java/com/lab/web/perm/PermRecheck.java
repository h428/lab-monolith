package com.lab.web.perm;

import com.lab.business.message.LabMessage;
import com.lab.business.threadlocal.LabIdThreadLocal;
import com.lab.common.exception.ParamErrorException;
import com.lab.common.util.ClassUtil;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * 提供统一方法，用于确保实体中的 labId 和最终查询或操作的实验室是一致的，将其称为“权限重验”，为什么需要权限重验：
 * 对于单记录查询以及删除接口，具体的 sql 并不和传入的 labId 相关，前端虽然传递了 labId，但可能存在下述权限漏洞：
 *  - 前端传递一个具备权限的 labId，此时 LabIdThreadLocal 解析并进一步鉴权，AOP 校验通过
 *  - 程序继续执行，但前端请求故意传递一个不属于该实验室的数据 id，比如请求了一个其他实验室的 roleId，这样就获取
 *  到了其他实验室的 labRole 数据，造成权限漏洞
 *
 *  如果在 Service 内部有做鉴权，直接从上下文取出 labId 并在 Service 方法内部添加上 labId 的过滤条件可以避免；
 *  但为了代码解耦以及各层作用明确，本项目统一在 controller 处理权限，Service 层只处理业务，因此需要权限重验；
 *  对于相关操作，需要在查询出数据后进一步重验证权限，调用本方法以利用数据的 labId 验证传入的 labId 合法
 *  例如查询出 labRole 后，进一步调用本方法验证 labRole.labId 和 LabIdThreadLocal 中的 labId 一致，
 *  则表示传参没问题，不一致则说明是恶意请求，抛出权限不足异常
 *
 *  另外如果统一获取 Controller 层结果并做统一的权限重验实际上是能做的，但这样会导致所有 listByLabId 之类的接口
 *  都冗余校验，影响性能（因为这类接口在查询时已经代上了 labId 条件，无需权限重验）；
 *  因此本项目采用折中的做法，在需要 recheckLabId 的 web 层接口，查询出数据后调用本类中的方法即可进行权限重验
 *
 *  一般需要权限重验的 web 层接口包括：
 *      - save 接口：
 *          如果是 create 的情况，不需要；
 *          如果是 update 的情况，需要根据 dataId 预查询数据，并做权限重验，才能做 update
 *      - deleteById 接口：需要根据 dataId 预查询数据，并做权限重验，才能做 delete
 *      - 查单个数据对象：在查询出对象后，做权限重验，才能返回结果
 *      - 根据 ids 查询多个对象：查询出结果后，需要对每个元素都做权限重验，才能返回结果
 *      - listByLabId 之类的接口：无需权限重验，过滤条件已经加了 labId 过滤条件
 *      - pageByLabId 之类的接口：无需权限重验，过滤条件已经加了 labId 的过滤条件
 */
public class PermRecheck {


    public static void recheckLabId(Object obj) {
        Long labId = LabIdThreadLocal.get();

        // 若传入的 labId 和数据对象的 labId 不一致，表示权限重验失败
        if (!Objects.equals(labId, ClassUtil.getValue(obj, "labId"))) {
            throw new ParamErrorException(LabMessage.LAB_ID_DATA_ID_INCONSISTENCY);
        }
    }

    public static void recheckLabId(Collection<?> collection) {
        collection.forEach(PermRecheck::recheckLabId);
    }

    // 更加方便的 recheck 方法，基于 lambda，但可读性会降低，本质上都是抽象各个 Controller 层的权限重验逻辑得到的

    public static <T> T recheckByDataId(Long dataId, Function<Long, T> getByIdMethod) {
        T data = getByIdMethod.apply(dataId);
        PermRecheck.recheckLabId(data);
        return data;
    }
}
