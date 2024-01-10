DROP TABLE IF EXISTS `user_login_tracker`;

CREATE TABLE `user_login_tracker`
(
    `id`                     char(36)    NOT NULL,
    `user_email`   char(36) not null,
    `user_ip`          char(36)   NOT NULL,
    `succeeded`          boolean  NOT NULL default false,
    `created_date_time`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_date_time` timestamp NULL     DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
