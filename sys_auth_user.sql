/*
 Navicat Premium Data Transfer

 Source Server         :
 Source Server Type    : MySQL
 Source Server Version : 100504
 Source Host           :
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 100504
 File Encoding         : 65001

 Date: 24/07/2020 15:26:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_user`;
CREATE TABLE `sys_auth_user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名称',
  `password` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `mobile_phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `state` tinyint(4) NOT NULL COMMENT '用户状态1.启用2.禁用',
  `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信openid\n',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL DEFAULT current_timestamp(0) COMMENT '创建时间',
  `last_modify_by` bigint(20) NOT NULL DEFAULT 0 COMMENT '上次修改人id',
  `last_modify_time` datetime(0) NOT NULL DEFAULT current_timestamp(0) COMMENT '上次修改时间',
  `logical_deleted` tinyint(4) NOT NULL DEFAULT 1 COMMENT '逻辑删除1.正常2.已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_auth_user
-- ----------------------------
INSERT INTO `sys_auth_user` VALUES (1, 'admin', '$2a$10$nRXBNoW/3vM/aSsmYsYRKelas6GcD9VSMm2gAA5412VoG6HaqYsTK', '11111111112', 1, '', '2020-06-27 10:14:50', 1, '2020-05-29 15:03:08', 2, '2020-06-05 10:37:48', 1);
INSERT INTO `sys_auth_user` VALUES (2, '张三', '456', '11111111111', 1, '', NULL, 1, '2020-06-01 10:49:01', 1, '2020-06-01 10:49:01', 1);
INSERT INTO `sys_auth_user` VALUES (3, '张无忌', '111', '11111111111', 1, '', NULL, 1, '2020-06-02 10:08:28', 1, '2020-06-02 10:08:28', 1);
INSERT INTO `sys_auth_user` VALUES (28, '2', '$2a$10$lNdX.LEcG6efs3syvwuBluor33JE.JO0UZPHT0SPwLx86sESnpuvG', '', 1, '', NULL, 2, '2020-06-09 08:58:33', 2, '2020-06-09 08:58:33', 1);
INSERT INTO `sys_auth_user` VALUES (29, '2', '$2a$10$6g8nKbg5rsMMFFPbWfC4Ru8nFyqqirP9lFot/J0oV6OjwgL49LDv.', '', 1, '555', NULL, 2, '2020-06-09 09:02:02', 2, '2020-06-09 09:02:02', 1);

SET FOREIGN_KEY_CHECKS = 1;
