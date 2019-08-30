DROP IF EXIST taskmanager;
CREATE tasksmanager;
USE tasksmanager;

CREATE TABLE IF NOT EXISTS `tasksmanager`.`company` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(12) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `tasksmanager`.`task` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `message` VARCHAR(100) NOT NULL,
  `date_create` TIMESTAMP NOT NULL,
  `date_complete` TIMESTAMP NULL DEFAULT NULL,
  `company_id` INT(11) NULL DEFAULT NULL,
  `status` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
    FOREIGN KEY (`company_id`)
    REFERENCES `tasksmanager`.`companies` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;