package com.lab.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.common.exception.ParamErrorException;
import com.lab.entity.BaseUser;
import com.lab.mapper.BaseUserMapper;
import com.lab.business.bo.BaseUserBO;
import com.lab.business.converter.BaseUserConverter;
import com.lab.business.ro.BaseUserLoginRO;
import com.lab.business.ro.BaseUserRegisterRO;
import com.lab.business.ro.BaseUserUpdatePasswordRO;
import com.lab.business.ro.BaseUserUpdateRO;
import com.lab.business.vo.BaseUserVO;
import com.lab.service.BaseUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements BaseUserService {


    @Autowired
    private BaseUserMapper baseUserMapper;

    private final BaseUserConverter baseUserConverter = BaseUserConverter.INSTANCE;

    @Override
    public void register(BaseUserRegisterRO baseUserRegisterRO) {

        baseUserRegisterRO.validatePassword();

        // 转换为 baseUser 并生成对应的 salt 和 password
        BaseUser baseUser = this.baseUserConverter.registerRoToEntity(baseUserRegisterRO);
        BaseUserBO baseUserBo = this.baseUserConverter.entityToBo(baseUser);
        // 生成密码
        baseUserBo.generateSaltAndPassword();
        // 拷贝处理后的业务参数
        BeanUtil.copyProperties(baseUserBo, baseUser);

        // 设置其他参数
        Long now = System.currentTimeMillis();
        baseUser.setCreateTime(now);
        baseUser.setUpdateTime(now);

        this.baseUserMapper.insert(baseUser);
    }


    @Override
    public BaseUserVO loginByEmail(BaseUserLoginRO baseUserLoginRO) {

        String email = baseUserLoginRO.getEmail();

        Assert.notEmpty(email);

        // 根据邮箱查询对象
        QueryWrapper<BaseUser> getByEmail = Wrappers.query(BaseUser.builder().email(email).build());
        BaseUser baseUserByEmail = this.baseUserMapper.selectOne(getByEmail);

        // 若邮箱未注册，直接返回 null 表示登录失败
        if (baseUserByEmail == null) {
            return null;
        }

        // 校验输入密码是否正确
        BaseUserBO baseUserBo = this.baseUserConverter.entityToBo(baseUserByEmail);
        if (baseUserBo.passwordValid(baseUserLoginRO.getPassword())) {
            // 密码正确则返回查询出的对象
            return baseUserConverter.entityToVo(baseUserByEmail);
        }

        // 密码错误，登录失败
        return null;
    }

    @Override
    public void update(BaseUserUpdateRO baseUserUpdateRO) {

        Long id = baseUserUpdateRO.getId();
        Assert.notNull(id);

        BaseUser baseUser = this.baseUserMapper.selectById(id);
        Assert.notNull(baseUser);


        String username = baseUserUpdateRO.getUsername();

        if (StrUtil.isNotEmpty(username) && !username.equals(baseUser.getUsername())) {
            // 确认用户名不被占用
            LambdaQueryWrapper<BaseUser> getByUserName = Wrappers.<BaseUser>lambdaQuery()
                .eq(BaseUser::getUsername, username)
                .ne(BaseUser::getId, id);
            BaseUser byUsername = baseUserMapper.selectOne(getByUserName);
            if (byUsername != null) {
                throw new ParamErrorException("用户名已被占用");
            }
            baseUser.setUsername(username);
        }

        long now = System.currentTimeMillis();
        baseUser.setUpdateTime(now);

        this.baseUserMapper.updateById(baseUser);
    }

    @Override
    public void updatePassword(BaseUserUpdatePasswordRO passwordDto) {
        // 声明必须参数不为空
        Assert.notNull(passwordDto.getId());
        Assert.notEmpty(passwordDto.getOldPassword());
        Assert.notEmpty(passwordDto.getPassword());
        Assert.notEmpty(passwordDto.getConfirmPassword());

        // 校验密码符合业务条件
        passwordDto.checkPasswordBusinessValid();

        // 查询用户并验证旧密码
        BaseUser baseUser = this.baseUserMapper.selectById(passwordDto.getId());
        BaseUserBO baseUserBoForPasswordCheck = this.baseUserConverter.entityToBo(baseUser);

        if (!baseUserBoForPasswordCheck.passwordValid(passwordDto.getOldPassword())) {
            throw new ParamErrorException("旧密码验证不通过");
        }

        // 使用 Bo 生成新密码
        BaseUserBO baseUserBoForUpdatePassword = this.baseUserConverter.updatePasswordDtoToBo(passwordDto);
        baseUserBoForUpdatePassword.generateSaltAndPassword();

        // 更新密码到 entity
        baseUser.setPassword(baseUserBoForUpdatePassword.getPassword());
        baseUser.setSalt(baseUserBoForUpdatePassword.getSalt());
        baseUser.setUpdateTime(System.currentTimeMillis());
    }

    @Override
    public void resetPassword(BaseUserUpdatePasswordRO passwordDto) {
        // 声明必须参数不为空
        Assert.notNull(passwordDto.getId());
        Assert.notEmpty(passwordDto.getPassword());
        Assert.notEmpty(passwordDto.getConfirmPassword());

        // 校验密码符合业务条件
        passwordDto.checkPasswordBusinessValid();

        BaseUser baseUser = this.baseUserMapper.selectById(passwordDto.getId());

        // 使用 Bo 生成新密码
        BaseUserBO baseUserBoForUpdatePassword = this.baseUserConverter.updatePasswordDtoToBo(passwordDto);
        baseUserBoForUpdatePassword.generateSaltAndPassword();

        // 更新密码到 entity
        baseUser.setPassword(baseUserBoForUpdatePassword.getPassword());
        baseUser.setSalt(baseUserBoForUpdatePassword.getSalt());
        baseUser.setUpdateTime(System.currentTimeMillis());
    }

    @Override
    public BaseUserVO get(Long id) {
        return this.baseUserConverter.entityToVo(super.getById(id));
    }

    @Override
    public boolean existByEmail(String email) {
        return this.baseUserMapper.existByEmail(email);
    }

    @Override
    public boolean existByUsername(String username) {
        return this.baseUserMapper.existByUsername(username);
    }

    @Override
    public List<BaseUserVO> listByIds(List<Long> ids) {
        return this.baseUserConverter.entityToVo(super.listByIds(ids));
    }
}
