package com.lab.common.component;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.lab.common.constant.TimeConstant;
import com.lab.common.constant.TokenTypeEnum;

public class TokenUtil {

    /**
     * 简单地放置 token 双向结构
     * @param tokenTypeEnum token 类型
     * @param userId 用户 id
     * @param token 对应 token
     */
    private static void putToken(TokenTypeEnum tokenTypeEnum, Long userId, String token) {
        // 保存 token 到 redis
        String tokenKey = tokenTypeEnum.TOKEN_PREFIX + token;
        RedisUtil.put(tokenKey, String.valueOf(userId), TimeConstant.SECONDS_ONE_HOUR);
    }

    private static String generateLoginTokenAndPutToRedis(TokenTypeEnum tokenTypeEnum, Long userId) {
        Assert.notNull(userId);

        // 使用 UUID 作为 token
        String token = IdUtil.simpleUUID();

        // 随机生成 tokenKey 作为 token
        String tokenKey;
        do {
            tokenKey = tokenTypeEnum.TOKEN_PREFIX + token;
        } while (RedisUtil.exist(tokenKey));

        // 放置 token 结构
        putToken(tokenTypeEnum, userId, token);

        return token;
    }


    /**
     * 刷新 token 并返回 token 对应的用户 id，模拟 HttpSession
     * @param token
     * @return 若 token 存在则刷新并返回 userId；不存在则返回 null 表示未登录
     */
    private static Long refresh(TokenTypeEnum tokenTypeEnum, String token) {

        String tokenKey = tokenTypeEnum.TOKEN_PREFIX + token;

        String userIdStr = RedisUtil.get(tokenKey);

        if (userIdStr == null) {
            return null;
        }

        // 放置 token 结构，使用原有 token 值即为刷新 token
        Long userId = Long.valueOf(userIdStr);
        putToken(tokenTypeEnum, userId, token);

        // 返回成功解析的 userId
        return userId;
    }

    /**
     * 根据 tokenType 和 token 移除 token
     * @param tokenTypeEnum
     * @param token
     */
    private static void removeToken(TokenTypeEnum tokenTypeEnum, String token) {
        String tokenKey = tokenTypeEnum.TOKEN_PREFIX + token;
        RedisUtil.remove(tokenKey);
    }


    /**
     * 执行 BaseUser 登录并返回 token
     * @param baseUserId baseUserId
     * @return token
     */
    public static String baseUserLogin(Long baseUserId) {
        return generateLoginTokenAndPutToRedis(TokenTypeEnum.BASE_USER, baseUserId);
    }

    /**
     * 根据 token 判断是否已登录
     * @param token 待判断的 token
     * @return 已登录则返回对应的 baseUserId；否则返回 null
     */
    public static Long isBaseUserLogin(String token) {
        return refresh(TokenTypeEnum.BASE_USER, token);
    }

    public static void baseUserLogout(String token) {
        removeToken(TokenTypeEnum.BASE_USER, token);
    }

    public static String adminLogin(Long adminId) {
        return generateLoginTokenAndPutToRedis(TokenTypeEnum.ADMIN, adminId);
    }

    public static Long isAdminLogin(String token) {
        return refresh(TokenTypeEnum.ADMIN, token);
    }

}
