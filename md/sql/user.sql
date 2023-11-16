/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : spring-boot-demo

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 03/01/2020 10:54:01
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `springboot_user`;
CREATE TABLE `springboot_user`
(
    `id`          bigint(11) NOT NULL COMMENT '主键',
    `username`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `gender`      varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'MALE' COMMENT '性别',
    `birthday`    datetime(0) DEFAULT NULL COMMENT '生日',
    `email`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
    `region`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'CHINA' COMMENT '区域',
    `status`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'ENABLED' COMMENT '状态',
    `create_time` datetime(0) NOT NULL DEFAULT NOW(),
    `update_time` datetime(0) NOT NULL DEFAULT NOW(),
    `is_deleted`  smallint(5) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '用户表' ROW_FORMAT = Dynamic ;

SET
FOREIGN_KEY_CHECKS = 1;

drop table `global_table`;
create table `global_table`
(
    `xid`                       varchar(128) not null,
    `transaction_id`            bigint,
    `status`                    tinyint      not null,
    `application_id`            varchar(64),
    `transaction_service_group` varchar(64),
    `transaction_name`          varchar(128),
    `timeout`                   int,
    `begin_time`                bigint,
    `application_data`          varchar(2000),
    `gmt_create`                datetime,
    `gmt_modified`              datetime,
    primary key (`xid`),
    key                         `idx_gmt_modified_status` (`gmt_modified`, `status`),
    key                         `idx_transaction_id` (`transaction_id`)
);

-- the table to store BranchSession data
drop table `branch_table`;
create table `branch_table`
(
    `branch_id`         bigint       not null,
    `xid`               varchar(128) not null,
    `transaction_id`    bigint,
    `resource_group_id` varchar(128),
    `resource_id`       varchar(256),
    `lock_key`          varchar(256),
    `branch_type`       varchar(8),
    `status`            tinyint,
    `client_id`         varchar(64),
    `application_data`  varchar(2000),
    `gmt_create`        datetime,
    `gmt_modified`      datetime,
    primary key (`branch_id`),
    key                 `idx_xid` (`xid`)
);

-- the table to store lock data
drop table `lock_table`;
create table `lock_table`
(
    `row_key`        varchar(128) not null,
    `xid`            varchar(128),
    `transaction_id` long,
    `branch_id`      long,
    `resource_id`    varchar(256),
    `table_name`     varchar(64),
    `pk`             varchar(128),
    `gmt_create`     datetime,
    `gmt_modified`   datetime,
    primary key (`row_key`)
);
