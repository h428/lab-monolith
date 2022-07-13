package com.lab.business.threadlocal;

import cn.hutool.core.util.StrUtil;
import com.lab.business.vo.LabEntryVO;
import com.lab.business.vo.LabRoleVO;
import com.lab.cache.LabPermCache;

public class LabPermContext {

    private static final ThreadLocal<Node> LOCAL = new ThreadLocal<>();

    public static Node init() {

        Node node = new Node();
        node.baseUserId = BaseUserIdThreadLocal.get();
        node.labId = LabIdThreadLocal.get();

        LOCAL.set(node);

        return node;
    }

    public static void remove() {
        LOCAL.remove();
    }

    static Node get() {
        Node node = LOCAL.get();

        if (node == null) {
            node = init();
        }

        return node;
    }


    public static boolean own() {
        Node node = get();

        if (node.own == null) {
            node.own = LabPermCache.own(node.baseUserId, node.labId);
        }

        return node.own;
    }

    public static boolean addedIn() {
        Node node = get();

        if (node.addedIn == null) {
            LabEntryVO addedInLabEntry = LabPermCache.getAddedInLabEntry(node.baseUserId, node.labId);

            // 如果已加入该实验室，进一步初始相关权限属性
            if (node.addedIn = addedInLabEntry != null) {
                node.labUserId = addedInLabEntry.getLabUserId();
                node.labRoleId = addedInLabEntry.getLabRoleId();
                node.labPerms = LabPermCache.getLabRole(node.labRoleId).getLabPerms();
            }
        }

        return node.addedIn;
    }

    public static Long labUserId() {
        Node node = get();

        if (addedIn()) {
            return node.labUserId;
        }

        // 为加入到指定实验室，但拥有指定实验室，则返回默认值 0
        if (own()) {
            return 0L;
        }

        return null;
    }

    public static Long labRoleId() {
        Node node = get();

        addedIn();

        return node.labRoleId;
    }

    public static boolean hasPerm(String perm) {
        Node node = get();

        addedIn();

        return StrUtil.isNotEmpty(node.labPerms) && node.labPerms.contains(perm);
    }

    public static class Node {
        Long baseUserId;
        Long labId;
        Boolean own;
        Boolean addedIn;
        Long labUserId;
        Long labRoleId;
        String labPerms;
    }

}
