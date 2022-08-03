# lab-monolith

## 项目介绍

Labmate 是一个实验室物料管理系统，为高校的各个实验室提供物料管理的功能。系统用户通过创建或者加入实验室成为某一实验室的成员，从而在实验室内部进行物料管理，包括物料的入库、消耗、申请、审批等操作。



本项目为 Labmate 单体重构版，基于最简单的 Web MVC 三层架构，提供 Labmate 最核心的功能，包括用户注册、用户登录、实验室管理、成员管理、角色和权限管理、物品管理、架子管理、库存管理和记录单管理等。

前端项目地址：[lab-web-system](https://github.com/h428/lab-web-system)

项目演示地址：[Labmate](http://124.221.132.55)；[lab-web-system](http://124.221.132.55:81/lab)



## 目录结构

```js
lab-monolith
├── lab-core 集成所有核心功能的 Service 以及 Util，供其他子系统复用
├── lab-system 提供 Labmate 用户子系统的 http 接口，依赖 lab-core
├── lab-admin // todo 超管子系统，用于观测用户数据和所有实验室状况，依赖 lab-core
```

其中，整个项目的各个 package 结构的具体含义如下：

```js
com.lab
├── business 和业务相关的一些 vo、常量和 message 等
├── cache 缓存
├── common 和业务无关的组件、工具和通用对象等
    ├── aop.param 基于 aop 的自定义 service 层入参校验
    ├── bean 通用 bean 对象（分页，web 层统一结果）
    ├── builder 通用对象的构造器
    ├── component 依赖 Spring 环境的通用工具类，将其称为 component
    ├── constant 全局通用常量
    ├── ro 通用的请求入参对象
    ├── util 通用工具类
├── config 基于 Spring Boot 的配置
├── entity 实体，和 table 一一对应
├── mapper 基于 Mybatis Plus 的 mapper
├── service 服务层
├── web web 层，其中 controller 基于 REST 提供 API，其他包为各个 web 组件
```



# 系统设计

## 技术选型

|技术|说明|官网|
| --- | --- | --- |
|SpringBoot|容器 + MVC 框架| https://spring.io/projects/spring-boot |
|MyBatis Plus|ORM 框架|https://baomidou.com |
|Redis|缓存| https://redis.io |
|Nginx|静态资源服务器| https://www.nginx.com |
|Lombok| 简化对象封装工具 | https://github.com/rzwitserloot/lombok |
|Mapstruct|对象转换工具| https://mapstruct.org |
|Hutool| Java 工具类库 | https://github.com/looly/hutool |
|Swagger-UI|文档生成工具| https://github.com/swagger-api/swagger-ui |
|Hibernator-Validator| 验证框架| http://hibernate.org/validator |

## 常规方案

- 数据库设计：见文件 [lab.sql](./doc/lab.sql)
- 缓存：采用 Redis 做缓存，Key 统一采用 String，Value 采用基于 JSON 的序列化方案
- 认证：基于 Redis 模拟 HTTP Session，基于 Interceptor 做统一拦截进行认证
- 鉴权：已认证的前提下，统一在 Web 层做鉴权，基于自定义注解配合 AOP 对 REST API 接口做业务鉴权，可参考 `lab-system/com.lab.web.perm` 包下的源码
- 参数校验与异常：
    - web 层入参校验基于 spring 提供的 @Validated 注解，service 出于灵活需要自定义 @ServiceValidate 注解做入参校验
    - 程序内部的可预知异常统一基于 BaseException 及其子类实现，并由 GlobalExceptionHandler 统一处理并转化为 json 提示，同时 GlobalExceptionHandler 还统一处理了由 @Validate 参数校验抛出的各类异常
- 统一结果：Web 层所有响应结果统一由 com.lab.common.bean.ResBean 包裹，即服务器只要连接上，HTTP 统一为 200，只有硬件级别的错误会得到 500；而接口的参数错误、业务错误或内部错误则由 ResBean 内部的 status 表示，以便前端易于区分

# todo

- Refactoring with micro-service and DDD