CREATE TABLE `br_permission`
(
    `id`          int         NOT NULL COMMENT 'id',
    `code`        varchar(50) NOT NULL COMMENT '权限编码',
    `name`        varchar(50) NULL     DEFAULT NULL COMMENT '权限名称',
    `is_disabled` bit(1)      NOT NULL DEFAULT b'0' COMMENT '是否禁用',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_br_permission_code` (`code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '权限表'
  ROW_FORMAT = Dynamic;

CREATE TABLE `br_role`
(
    `id`          int         NOT NULL COMMENT 'id',
    `code`        varchar(50) NOT NULL COMMENT '角色编码',
    `name`        varchar(50) NULL     DEFAULT NULL COMMENT '角色名称',
    `is_disabled` bit(1)      NOT NULL DEFAULT b'0' COMMENT '是否禁用',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_br_role_code` (`code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

CREATE TABLE `br_role_permission`
(
    `id`              int UNSIGNED NOT NULL AUTO_INCREMENT,
    `role_code`       varchar(50)  NOT NULL COMMENT '角色编码',
    `permission_code` varchar(50)  NOT NULL COMMENT '权限编码',
    `is_disabled`     bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否禁用',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_br_role_permission_br_role_1` (`role_code`) USING BTREE,
    INDEX `fk_br_role_permission_br_permission_1` (`permission_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色权限表'
  ROW_FORMAT = Dynamic;

CREATE TABLE `br_user`
(
    `id`            bigint UNSIGNED NOT NULL COMMENT 'id',
    `username`      varchar(50)     NOT NULL COMMENT '用户名',
    `password`      varchar(255)    NOT NULL COMMENT '密码',
    `email`         varchar(100)    NULL     DEFAULT NULL COMMENT '邮箱',
    `register_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `is_disabled`   bit(1)          NOT NULL DEFAULT b'0' COMMENT '是否禁用',
    `is_deleted`    bit(1)          NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `created_by`    bigint          NOT NULL COMMENT '创建人',
    `created_at`    datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by`   bigint          NOT NULL COMMENT '更新人',
    `modified_at`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

CREATE TABLE `br_user_role`
(
    `id`          int UNSIGNED    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint UNSIGNED NOT NULL COMMENT '用户 id',
    `role_code`   varchar(50)     NOT NULL COMMENT '角色编码',
    `is_disabled` bit(1)          NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_br_user_role_br_user_1` (`user_id`) USING BTREE,
    INDEX `fk_br_user_role_br_role_1` (`role_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户角色表'
  ROW_FORMAT = Dynamic;

ALTER TABLE `br_role_permission`
    ADD CONSTRAINT `fk_br_role_permission_br_role_1` FOREIGN KEY (`role_code`) REFERENCES `br_role` (`code`);
ALTER TABLE `br_role_permission`
    ADD CONSTRAINT `fk_br_role_permission_br_permission_1` FOREIGN KEY (`permission_code`) REFERENCES `br_permission` (`code`);
ALTER TABLE `br_user_role`
    ADD CONSTRAINT `fk_br_user_role_br_user_1` FOREIGN KEY (`user_id`) REFERENCES `br_user` (`id`);
ALTER TABLE `br_user_role`
    ADD CONSTRAINT `fk_br_user_role_br_role_1` FOREIGN KEY (`role_code`) REFERENCES `br_role` (`code`);

