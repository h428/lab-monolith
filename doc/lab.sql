/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : lab

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 18/07/2022 09:10:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `base_user_id` bigint(20) NOT NULL COMMENT '基础用户 id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `organization` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '授权类型',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '授权状态',
  `allow_hub_user_use` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否允许我的仓库用户使用该授权信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_base_user_id`(`base_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '授权用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电子邮箱',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盐值',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '电话',
  `wechat_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信号',
  `avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '注册时间',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `last_login_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '上次登录时间',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
  `allow_add` bit(1) NOT NULL DEFAULT b'1' COMMENT '允许添加',
  `allow_auth` bit(1) NOT NULL DEFAULT b'1' COMMENT '允许将认证信息供仓库用户使用',
  `allow_message` bit(1) NOT NULL DEFAULT b'1' COMMENT '允许站内信',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_email`(`email`) USING BTREE,
  UNIQUE INDEX `idx_user_name`(`username`) USING BTREE,
  INDEX `idx_phone`(`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_order
-- ----------------------------
DROP TABLE IF EXISTS `group_order`;
CREATE TABLE `group_order`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `op_base_user_id` bigint(20) NOT NULL COMMENT '发起人',
  `auth_user_id` bigint(20) NOT NULL COMMENT '使用的授权用户',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '内容',
  `item_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '货号',
  `cas` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'CAS 编号',
  `total_num` int(11) NOT NULL COMMENT '总数量',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_op_base_user_id`(`op_base_user_id`) USING BTREE,
  INDEX `idx_auth_user_id`(`auth_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拼单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_order_confirm
-- ----------------------------
DROP TABLE IF EXISTS `group_order_confirm`;
CREATE TABLE `group_order_confirm`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `group_order_id` bigint(20) NOT NULL COMMENT '拼单 id',
  `op_base_user_id` bigint(20) NOT NULL COMMENT '拼单人',
  `auth_user_id` bigint(20) NOT NULL COMMENT '使用的授权用户',
  `num` int(11) NOT NULL COMMENT '拼单数量',
  `info` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  `confirm_time` bigint(20) NOT NULL COMMENT '确认时间',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_group_order_id`(`group_order_id`) USING BTREE,
  INDEX `idx_op_base_user_id`(`op_base_user_id`) USING BTREE,
  INDEX `idx_auth_user_id`(`auth_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拼单确认项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_order_message
-- ----------------------------
DROP TABLE IF EXISTS `group_order_message`;
CREATE TABLE `group_order_message`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `base_user_id` bigint(20) NOT NULL COMMENT '回复人',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '内容',
  `reply_time` bigint(20) NOT NULL COMMENT '回复时间',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_base_user_id`(`base_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拼单回复项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab
-- ----------------------------
DROP TABLE IF EXISTS `lab`;
CREATE TABLE `lab`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `desc_info` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述信息',
  `belong_base_user_id` bigint(20) NOT NULL COMMENT '所属用户 id',
  `create_base_user_id` bigint(20) NOT NULL COMMENT '创建用户 id',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_base_user_id` bigint(20) NOT NULL COMMENT '更新用户 id',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '最后更新时间',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_belong_user_id`(`belong_base_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_apply
-- ----------------------------
DROP TABLE IF EXISTS `lab_apply`;
CREATE TABLE `lab_apply`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `lab_in_id` bigint(20) NOT NULL COMMENT '入库时分配的唯一id',
  `capacity_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '每件容量品消耗后生成的 id',
  `num` int(11) NOT NULL COMMENT '数量',
  `op_info` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '申请归还描述',
  `op_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作时间',
  `confirm_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '确认时间',
  `from_lab_shelf_id` bigint(20) NOT NULL COMMENT '发起操作的架子',
  `to_lab_shelf_id` bigint(20) NOT NULL COMMENT '与操作相关的另一个架子',
  `from_lab_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '冗余列，发起操作的用户，优化查询',
  `to_lab_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '冗余列，与操作相关的另一个用户，优化查询',
  `confirm_lab_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '审批人',
  `move` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否移动操作',
  `lab_item_id` bigint(20) NOT NULL COMMENT '物品，冗余列，优化查询',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '申请/归还单状态',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_lab_id_and_op_time`(`lab_id`, `op_time`) USING BTREE,
  INDEX `idx_origin_id_and_op_time`(`lab_in_id`, `op_time`) USING BTREE,
  INDEX `idx_from_lab_user_id_and_op_time`(`from_lab_user_id`, `op_time`) USING BTREE,
  INDEX `idx_to_lab_user_id_and_op_time`(`to_lab_user_id`, `op_time`) USING BTREE,
  INDEX `idx_lab_item_id_and_op_time`(`lab_item_id`, `op_time`) USING BTREE,
  INDEX `idx_from_lab_shelf_id`(`from_lab_shelf_id`) USING BTREE,
  INDEX `idx_to_lab_shelf_id`(`to_lab_shelf_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '申请/归还' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_buy_apply
-- ----------------------------
DROP TABLE IF EXISTS `lab_buy_apply`;
CREATE TABLE `lab_buy_apply`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `hub_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `hub_user_id` bigint(20) NOT NULL COMMENT '申请用户',
  `buy_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '购买的物品名称',
  `desc_info` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '购买申请描述信息',
  `need_num` int(11) NOT NULL COMMENT '所需数量',
  `apply_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '申请时间',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '申请单状态',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `del_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_hub_id`(`hub_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '购买申请单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_in
-- ----------------------------
DROP TABLE IF EXISTS `lab_in`;
CREATE TABLE `lab_in`  (
  `id` bigint(20) NOT NULL COMMENT '入库时分配的唯一id',
  `lab_item_id` bigint(20) NOT NULL COMMENT '对应的物品',
  `num` int(11) NOT NULL COMMENT '数量',
  `price` int(11) NOT NULL DEFAULT 0 COMMENT '价格',
  `op_info` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库描述',
  `op_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '入库时间',
  `op_lab_shelf_id` bigint(20) NOT NULL COMMENT '入库架子',
  `op_lab_user_id` bigint(20) NOT NULL COMMENT '入库用户',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_lab_id_and_op_time`(`lab_id`, `op_time`) USING BTREE,
  INDEX `idx_lab_item_id_and_op_time`(`lab_item_id`, `op_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_inventory
-- ----------------------------
DROP TABLE IF EXISTS `lab_inventory`;
CREATE TABLE `lab_inventory`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `lab_shelf_id` bigint(20) NOT NULL COMMENT '所属架子',
  `lab_in_id` bigint(20) NOT NULL COMMENT '入库或首次申请时分配到的id',
  `capacity_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '每件容量品消耗后生成的 id',
  `num` int(11) NOT NULL COMMENT '数量',
  `capacity` int(11) NOT NULL DEFAULT 0 COMMENT '容量',
  `lab_id` bigint(20) NOT NULL COMMENT '冗余列：所属仓库',
  `lab_item_id` bigint(20) NOT NULL COMMENT '冗余列：物品 id',
  `lab_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '冗余列：实验室用户 id',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_real_id`(`lab_shelf_id`, `lab_in_id`, `capacity_id`) USING BTREE,
  INDEX `idx_lab_id`(`lab_id`) USING BTREE,
  INDEX `idx_lab_user_id`(`lab_user_id`) USING BTREE,
  INDEX `idx_lab_item_id`(`lab_item_id`) USING BTREE,
  INDEX `idx_lab_in_id`(`lab_in_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库架子物品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_item
-- ----------------------------
DROP TABLE IF EXISTS `lab_item`;
CREATE TABLE `lab_item`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `eng_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '英文名',
  `desc_info` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述信息',
  `price` int(11) NOT NULL DEFAULT 0 COMMENT '单价(分)',
  `unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计量单位',
  `item_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '货号',
  `cas` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'cas 编号',
  `floated` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否支持浮点数操作',
  `update_lab_user_id` bigint(20) NOT NULL COMMENT '更新用户',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `capacity` int(11) NOT NULL DEFAULT 0 COMMENT '规格品的规格量，0 表示非规格品',
  `capacity_unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规格品单位',
  `create_lab_user_id` bigint(20) NOT NULL COMMENT '创建用户',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_lab_id`(`lab_id`) USING BTREE,
  FULLTEXT INDEX `idx_name`(`name`, `eng_name`, `desc_info`) WITH PARSER `ngram`
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库物品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_join_link
-- ----------------------------
DROP TABLE IF EXISTS `lab_join_link`;
CREATE TABLE `lab_join_link`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `link` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '链接',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `lab_role_id` bigint(20) NOT NULL COMMENT '授权角色',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `create_lab_user_id` bigint(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_link`(`link`) USING BTREE,
  INDEX `idx_hub_id`(`lab_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '添加到仓库的链接' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_out
-- ----------------------------
DROP TABLE IF EXISTS `lab_out`;
CREATE TABLE `lab_out`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `lab_in_id` bigint(20) NOT NULL COMMENT '入库时分配的唯一id',
  `capacity_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '单个容量品消耗后生成的 id',
  `num` int(11) NOT NULL COMMENT '数量',
  `op_info` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消耗描述',
  `op_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作时间',
  `op_lab_shelf_id` bigint(20) NOT NULL COMMENT '消耗架子',
  `op_lab_user_id` bigint(20) NOT NULL COMMENT '消耗用户',
  `lab_item_id` bigint(20) NOT NULL COMMENT '物品，冗余列，用于优化查询',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_hub_id_and_op_time`(`lab_id`, `op_time`) USING BTREE,
  INDEX `idx_origin_id_and_op_time`(`lab_in_id`, `op_time`) USING BTREE,
  INDEX `idx_op_hub_user_id_and_op_time`(`op_lab_user_id`, `op_time`) USING BTREE,
  INDEX `idx_hub_item_id_and_op_time`(`lab_item_id`, `op_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消耗' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_perm
-- ----------------------------
DROP TABLE IF EXISTS `lab_perm`;
CREATE TABLE `lab_perm`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `tag` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标记',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '类别id，0表示该行是类别',
  `is_nav` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否导航栏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限，稳定的权限，和操作有关，和 hub 无关' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_role
-- ----------------------------
DROP TABLE IF EXISTS `lab_role`;
CREATE TABLE `lab_role`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `desc_info` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述信息',
  `lab_perms` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限列表，使用 , 分隔',
  `create_lab_user_id` bigint(20) NOT NULL COMMENT '更新用户 id',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_lab_user_id` bigint(20) NOT NULL COMMENT '更新用户 id',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_hub_id`(`lab_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_shelf
-- ----------------------------
DROP TABLE IF EXISTS `lab_shelf`;
CREATE TABLE `lab_shelf`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `pos` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '位置',
  `type` tinyint(4) NOT NULL COMMENT '类型：0 个人；1 公共；2 贮藏',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `belong_lab_user_id` bigint(20) NOT NULL COMMENT '架子所属用户，非私人架子则为0',
  `create_lab_user_id` bigint(20) NOT NULL COMMENT '创建用户',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_lab_user_id` bigint(20) NOT NULL COMMENT '更新用户',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_hub_id`(`lab_id`) USING BTREE,
  INDEX `idx_hub_user_id`(`belong_lab_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库架子' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lab_user
-- ----------------------------
DROP TABLE IF EXISTS `lab_user`;
CREATE TABLE `lab_user`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `lab_id` bigint(20) NOT NULL COMMENT '所属仓库',
  `base_user_id` bigint(20) NOT NULL COMMENT '可用用户',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户在该仓库的名称',
  `lab_role_id` bigint(20) NOT NULL COMMENT '用户角色 id',
  `create_lab_user_id` bigint(20) NOT NULL COMMENT '创建用户 id',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_lab_user_id` bigint(20) NOT NULL COMMENT '更新用户 id',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `delete_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_lab_id`(`lab_id`) USING BTREE,
  INDEX `idx_base_user_id`(`base_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库可用用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for school_user
-- ----------------------------
DROP TABLE IF EXISTS `school_user`;
CREATE TABLE `school_user`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `auth_user_id` bigint(20) NOT NULL COMMENT '授权用户 id',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '类型，如学生、教师等',
  `no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校或者教工号',
  `school` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校',
  `college` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学院',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_auth_user_id`(`auth_user_id`) USING BTREE,
  INDEX `idx_school`(`school`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学校用户，授权类型为院校类型的用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
