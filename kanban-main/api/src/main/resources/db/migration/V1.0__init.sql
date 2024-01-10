SET NAMES utf8;
SET
    time_zone = '+00:00';
SET
    sql_mode = 'NO_AUTO_VALUE_ON_ZERO';


CREATE
    DATABASE IF NOT EXISTS `${schema}` DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;

USE
`${schema}`;

DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization`
(
    `id`                 char(36)    NOT NULL,
    `name`         varchar(50) NULL,
    `description`          varchar(500) NOT NULL,
    `domain`          varchar(50) NOT NULL,
    `disabled`            boolean     NOT NULL default false,
    `created_date_time`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_date_time` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `org_name_unique` (`name`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`                 char(36)    NOT NULL,
    `first_name`         varchar(50) NULL,
    `last_name`          varchar(50) NOT NULL,
    `email`              varchar(50) NOT NULL,
    `password`           varchar(500),
    `role_name`          varchar(500) NOT NULL DEFAULT 'ROLE_USER',
    `disabled`            boolean     NOT NULL default false,
    `created_date_time`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_date_time` timestamp NULL DEFAULT NULL,
    `organization_id`   char(36) not null references organization(`id`),
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_email_unique` (`email`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `user_password_reset_request`;

CREATE TABLE `user_password_reset_request`
(
    `id`                     char(36)    NOT NULL,
    `user_id`                char(36)    NOT NULL,
    `expire_date_time`       timestamp not null,
    `created_date_time`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_date_time` timestamp NULL     DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `forecast_dash_board`;

CREATE TABLE `forecast_dash_board`
(
    `id`                     char(36)    NOT NULL,
    `organization_id`   char(36) not null references organization(`id`),
    `dash_board_id`          char(36)   NOT NULL,
    `created_date_time`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_date_time` timestamp NULL     DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

-- kanban item

-- Create KanbanTask Table
DROP TABLE IF EXISTS `kanban_task`;

CREATE TABLE `kanban_task` (
  `id` VARCHAR(255) PRIMARY KEY,
  `title` VARCHAR(255) NOT NULL,
  `status_id` VARCHAR(255) NOT NULL,
  `completed` boolean NOT NULL,
  `created_date_time`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_date_time` timestamp NULL     DEFAULT NULL
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

