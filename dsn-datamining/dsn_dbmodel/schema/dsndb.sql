
DROP TABLE IF EXISTS `commonwords`;

CREATE TABLE `commonwords`
(
`cword_id` INT NOT NULL AUTO_INCREMENT,
`cword` varchar(15) NOT NULL,
PRIMARY KEY(`cword_id`),
UNIQUE INDEX(`cword`)
)ENGINE=InnoDB;