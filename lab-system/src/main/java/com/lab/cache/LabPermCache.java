package com.lab.cache;

import cn.hutool.core.util.StrUtil;
import com.lab.business.vo.LabEntryVO;
import com.lab.business.vo.LabRoleVO;
import com.lab.common.component.RedisUtil;
import com.lab.common.component.SpringContextUtil;
import com.lab.common.constant.TimeConstant;
import com.lab.service.impl.LabRoleServiceImpl;
import com.lab.service.impl.LabServiceImpl;
import java.util.Map;

public class LabPermCache {

    private static LabRoleServiceImpl labRoleService;
    private static LabServiceImpl labService;

    // 缓存秒数
    private static long PERM_CACHE_TIME = TimeConstant.SECONDS_OF_HALF_HOUR;

    static {
        labRoleService = SpringContextUtil.getBean(LabRoleServiceImpl.class);
        labService = SpringContextUtil.getBean(LabServiceImpl.class);
    }

    // 便捷方法
    public static boolean userHasPerm(Long baseUserId, Long labId, String perm) {

        // 为加入到指定的实验室，则具备权限
        if (!addedIn(baseUserId, labId)) {
            return false;
        }

        // 否则查询出角色 id
        final String key = ADDED_IN_LAB_ENTRY_PREFIX + baseUserId;
        final String hashKey = String.valueOf(labId);
        LabEntryVO labEntryVO = RedisUtil.hashGet(key, hashKey);
        Long labRoleId = labEntryVO.getLabRoleId();

        // 判断指定的角色是否具有对应权限
        return roleHasPerm(labRoleId, perm);
    }

    public static boolean roleHasPerm(Long roleId, String perm) {

        LabRoleVO labRole = getLabRole(roleId);

        // 角色不存在，返回 false
        if (labRole == null) {
            return false;
        }

        // 判断是否包含指定权限
        String labPerms = labRole.getLabPerms();
        return StrUtil.isNotEmpty(labPerms) && labPerms.contains(perm);
    }

    public static boolean addedIn(Long baseUserId, Long labId) {
        final String key = ADDED_IN_LAB_ENTRY_PREFIX + baseUserId;

        if (!RedisUtil.exist(key)) {
            cacheAddedInLabEntry(baseUserId);
        }

        return RedisUtil.hashExist(key, String.valueOf(labId));
    }

    public static Long getAddedInLabUserId(Long baseUserId, Long labId) {
        return getAddedInLabEntry(baseUserId, labId).getLabUserId();
    }

    public static LabEntryVO getAddedInLabEntry(Long baseUserId, Long labId) {
        final String key = ADDED_IN_LAB_ENTRY_PREFIX + baseUserId;

        if (!RedisUtil.exist(key)) {
            cacheAddedInLabEntry(baseUserId);
        }

        return RedisUtil.hashGet(key, String.valueOf(labId));
    }

    public static boolean own(Long baseUserId, Long labId) {
        final String key = OWN_LAB_ENTRY_PREFIX + baseUserId;

        if (!RedisUtil.exist(key)) {
            cacheOwnLabEntry(baseUserId);
        }

        return RedisUtil.hashExist(key,  String.valueOf(labId));
    }

    // 缓存相关方法

    public static final String ADDED_IN_LAB_ENTRY_PREFIX = "ADDED_IN_LAB_ENTRY_PREFIX:";

    public static void cacheAddedInLabEntry(Long baseUserId) {
        final String key = ADDED_IN_LAB_ENTRY_PREFIX + baseUserId;

        Map<Long, LabEntryVO> addedInLabEntryMap = labService.findAddedInLabEntryMap(baseUserId);

        if (addedInLabEntryMap.isEmpty()) {
            RedisUtil.hashPut(key, "none", "1", PERM_CACHE_TIME);
            return;
        }

        addedInLabEntryMap.forEach((labId, labEntry) ->
            RedisUtil.hashPut(key, String.valueOf(labId), labEntry, PERM_CACHE_TIME));
    }

    public static void removeAddedInLabEntry(Long baseUserId) {
        final String key = ADDED_IN_LAB_ENTRY_PREFIX + baseUserId;
        RedisUtil.remove(key);
    }


    public static final String OWN_LAB_ENTRY_PREFIX = "OWN_LAB_ENTRY_PREFIX:";

    public static void cacheOwnLabEntry(Long baseUserId) {
        final String key = OWN_LAB_ENTRY_PREFIX + baseUserId;

        Map<Long, LabEntryVO> ownLabEntryMap = labService.findOwnLabEntryMap(baseUserId);

        if (ownLabEntryMap.isEmpty()) {
            RedisUtil.hashPut(key, "none", "1", PERM_CACHE_TIME);
            return;
        }

        ownLabEntryMap.forEach((labId, value) ->
            RedisUtil.hashPut(key, String.valueOf(labId), "1", PERM_CACHE_TIME));
    }

    public static void removeOwnLabEntry(Long baseUserId) {
        final String key = OWN_LAB_ENTRY_PREFIX + baseUserId;
        RedisUtil.remove(key);
    }


    private static final String LAB_ROLE_PREFIX = "LAB_ROLE:";

    public static LabRoleVO getLabRole(Long roleId) {
        final String key = LAB_ROLE_PREFIX + roleId;

        LabRoleVO labRoleVO = RedisUtil.get(key);

        // 该角色已经缓存，返回缓存角色
        if (labRoleVO != null) {
            return labRoleVO;
        }

        // 未缓存则查询角色并缓存，最后返回对应角色，不存在会返回 null
        return cacheLabRole(roleId);
    }

    public static LabRoleVO cacheLabRole(Long roleId) {
        LabRoleVO roleDto = labRoleService.get(roleId);
        final String key = LAB_ROLE_PREFIX + roleId;
        RedisUtil.put(key, roleDto, PERM_CACHE_TIME);
        return roleDto;
    }

    public static void removeLabRole(Long roleId) {
        final String key = LAB_ROLE_PREFIX + roleId;
        RedisUtil.remove(key);
    }


}
