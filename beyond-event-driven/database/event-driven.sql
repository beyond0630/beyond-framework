CREATE DATABASE IF NOT EXISTS `event-driven` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

USE `event-driven`;

CREATE TABLE `inbox_handled` (
    `id`         char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID，Hex(MD5(QueueName+MessageID))+Hex(DataLength) [PK]',
    `message_id` char(32) COLLATE utf8mb4_unicode_ci                       NOT NULL DEFAULT '' COMMENT '消息 ID [FK|amq_inbox_message.message_id]',
    `created_at` datetime                                                  NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录创建时间',
    PRIMARY KEY (`id`),
    KEY `ix_message_id`(`message_id`),
    KEY `ix_created_at`(`created_at`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT ='RabbitMQ 已处理的消息';

CREATE TABLE `inbox_message` (
    `id`           bigint unsigned                         NOT NULL COMMENT 'ID [PK]',
    `message_id`   char(32) COLLATE utf8mb4_unicode_ci     NOT NULL DEFAULT '' COMMENT '消息 ID',
    `is_binary`    tinyint unsigned                        NOT NULL DEFAULT '0' COMMENT '是否二进制消息 [Bool|0:否, 1:是]',
    `exchange`     varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'RabbitMQ Exchange',
    `routing_key`  varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'RabbitMQ RoutingKey',
    `content_type` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '内容类型, MIME-Type',
    `message_type` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消息类型',
    `created_at`   datetime                                NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录创建时间',
    `properties`   text COLLATE utf8mb4_unicode_ci COMMENT '消息属性',
    `header`       text COLLATE utf8mb4_unicode_ci COMMENT '消息头',
    `body`         mediumtext COLLATE utf8mb4_unicode_ci COMMENT '消息体',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_message_id`(`message_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT ='RabbitMQ 接收消息记录';

CREATE TABLE `outbox_message` (
    `id`           bigint unsigned                         NOT NULL COMMENT 'ID [PK]',
    `message_id`   char(32) COLLATE utf8mb4_unicode_ci     NOT NULL DEFAULT '' COMMENT '消息 ID',
    `is_binary`    tinyint unsigned                        NOT NULL DEFAULT '0' COMMENT '是否二进制消息 [Bool|0:否, 1:是]',
    `exchange`     varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'RabbitMQ Exchange',
    `routing_key`  varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'RabbitMQ RoutingKey',
    `content_type` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '内容类型, MIME-Type',
    `message_type` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消息类型',
    `created_at`   datetime                                NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录创建时间',
    `properties`   text COLLATE utf8mb4_unicode_ci COMMENT '消息属性',
    `header`       text COLLATE utf8mb4_unicode_ci COMMENT '消息头',
    `body`         mediumtext COLLATE utf8mb4_unicode_ci COMMENT '消息体',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_message_id`(`message_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT ='RabbitMQ 发送消息记录';

CREATE TABLE `outbox_returned` (
    `id`          char(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID [PK][FK|amq_outbox_message.message_id]',
    `retries`     int unsigned                        NOT NULL DEFAULT '0' COMMENT '已重试次数',
    `next_retry`  datetime                            NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下次重试时间',
    `created_at`  datetime                            NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录创建时间',
    `updated_at`  datetime                            NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '最后更新时间',
    `row_version` int unsigned                        NOT NULL DEFAULT '0' COMMENT '记录行的版本号 [RowVersion]',
    PRIMARY KEY (`id`),
    KEY `ix_next_retry`(`next_retry`),
    KEY `ix_created_at`(`created_at`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT ='RabbitMQ 中未被路由到队列的消息';

CREATE TABLE `outbox_unconfirmed` (
    `id`          char(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID [PK][FK|amq_outbox_message.message_id]',
    `retries`     int unsigned                        NOT NULL DEFAULT '0' COMMENT '已重试次数',
    `next_retry`  datetime                            NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下次重试时间',
    `created_at`  datetime                            NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录创建时间',
    `updated_at`  datetime                            NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '最后更新时间',
    `row_version` int unsigned                        NOT NULL DEFAULT '0' COMMENT '记录行的版本号 [RowVersion]',
    PRIMARY KEY (`id`),
    KEY `ix_next_retry`(`next_retry`),
    KEY `ix_created_at`(`created_at`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT ='RabbitMQ 未收到服务端确认的消息';