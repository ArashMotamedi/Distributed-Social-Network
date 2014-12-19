
DROP TABLE IF EXISTS `acounts`;
DROP TABLE IF EXISTS `service_providers`;
DROP TABLE IF EXISTS `status_messages`;

CREATE TABLE `accounts`
(
`account_id` INT NOT NULL AUTO_INCREMENT, 
`user_id` INT NOT NULL,
`sp_id` VARCHAR(2) NOT NULL,
`sp_login_id` VARCHAR(50) NOT NULL,
`access_token` VARCHAR(200),
`secret_token` VARCHAR(200),
`last_updated` DATETIME,
PRIMARY KEY(`account_id`),
UNIQUE INDEX(`user_id`, `sp_id`)
)ENGINE=InnoDB;

CREATE TABLE `service_providers`
(
`sp_id` VARCHAR(2) NOT NULL,
`sp_name` VARCHAR(50) NOT NULL,
PRIMARY KEY(`sp_id`),
UNIQUE INDEX(`sp_id`)
)ENGINE=InnoDB;

CREATE TABLE `status_messages`
(
`sm_id` INT NOT NULL AUTO_INCREMENT,
`user_id` INT NOT NULL,
`sp_id` VARCHAR(2) NOT NULL,
`status` VARCHAR(500) NOT NULL,
`timestamp` DATETIME,
`sm_sp_id` VARCHAR(100),
PRIMARY KEY(`sm_id`),
UNIQUE INDEX(`sm_id`)
)ENGINE=InnoDB;

CREATE UNIQUE INDEX `messages` ON status_messages (`sp_id`, `sm_sp_id`);
