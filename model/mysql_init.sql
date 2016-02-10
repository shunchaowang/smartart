SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `template` ;
CREATE SCHEMA IF NOT EXISTS `template` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
GRANT ALL ON template.* TO 'admin'@'localhost' IDENTIFIED BY 'admin';
FLUSH PRIVILEGES;
USE `template` ;

-- -----------------------------------------------------
-- Table `template`.`USER_STATUSES`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`USER_STATUSES` ;

CREATE TABLE IF NOT EXISTS `template`.`USER_STATUSES` (
  `USST_ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `USST_NAME` VARCHAR(32) NOT NULL,
  `USST_DESCRIPTION` TEXT NULL,
  `USST_ACTIVE` TINYINT NOT NULL,
  `USST_CODE` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`USST_ID`),
  UNIQUE INDEX `USST_ID_UNIQUE` (`USST_ID` ASC),
  UNIQUE INDEX `USST_NAME_UNIQUE` (`USST_NAME` ASC),
  UNIQUE INDEX `USST_CODE_UNIQUE` (`USST_CODE` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `template`.`USERS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`USERS` ;

CREATE TABLE IF NOT EXISTS `template`.`USERS` (
  `USER_ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `USER_USERNAME` VARCHAR(64) NOT NULL,
  `USER_PASSWORD` VARCHAR(127) NOT NULL,
  `USER_FIRST_NAME` VARCHAR(32) NOT NULL,
  `USER_LAST_NAME` VARCHAR(32) NOT NULL,
  `USER_EMAIL` VARCHAR(32) NOT NULL,
  `USER_PROFILE_IMAGE` BLOB NULL,
  `USER_REMARK` TEXT NULL,
  `USER_CREATED_TIME` TIMESTAMP NOT NULL,
  `USER_UPDATED_TIME` TIMESTAMP NULL,
  `USER_ACTIVE` TINYINT NOT NULL,
  `USER_MCHT_ID` BIGINT UNSIGNED NULL,
  `USER_USST_ID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`USER_ID`, `USER_USST_ID`),
  INDEX `fk_USER_USST_idx` (`USER_USST_ID` ASC),
  UNIQUE INDEX `USER_ID_UNIQUE` (`USER_ID` ASC),
  UNIQUE INDEX `USER_EMAIL_UNIQUE` (`USER_EMAIL` ASC),
  CONSTRAINT `fk_USER_USST`
    FOREIGN KEY (`USER_USST_ID`)
    REFERENCES `template`.`USER_STATUSES` (`USST_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `template`.`ROLES`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`ROLES` ;

CREATE TABLE IF NOT EXISTS `template`.`ROLES` (
  `ROLE_ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` VARCHAR(64) NOT NULL,
  `ROLE_DESCRIPTION` TEXT NULL,
  `ROLE_ACTIVE` TINYINT NOT NULL,
  `ROLE_CODE` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`ROLE_ID`),
  UNIQUE INDEX `ROLE_ID_UNIQUE` (`ROLE_ID` ASC),
  UNIQUE INDEX `ROLE_NAME_UNIQUE` (`ROLE_NAME` ASC),
  UNIQUE INDEX `ROLE_CODE_UNIQUE` (`ROLE_CODE` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `template`.`MENU_CATEGORIES`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`MENU_CATEGORIES` ;

CREATE TABLE IF NOT EXISTS `template`.`MENU_CATEGORIES` (
  `MNCG_ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `MNCG_NAME` VARCHAR(64) NOT NULL,
  `MNCG_INDEX` INT NOT NULL,
  PRIMARY KEY (`MNCG_ID`),
  UNIQUE INDEX `MNCG_ID_UNIQUE` (`MNCG_ID` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `template`.`MENU_ITEMS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`MENU_ITEMS` ;

CREATE TABLE IF NOT EXISTS `template`.`MENU_ITEMS` (
  `MENU_ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `MENU_NAME` VARCHAR(64) NOT NULL,
  `MENU_INDEX` INT UNSIGNED NOT NULL,
  `MENU_TARGET` VARCHAR(127) NOT NULL,
  `MENU_MNCG_ID` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`MENU_ID`),
  UNIQUE INDEX `MN_ID_UNIQUE` (`MENU_ID` ASC),
  INDEX `FK_MENU_MNCG_ID_idx` (`MENU_MNCG_ID` ASC),
  CONSTRAINT `FK_MENU_MNCG_ID`
    FOREIGN KEY (`MENU_MNCG_ID`)
    REFERENCES `template`.`MENU_CATEGORIES` (`MNCG_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `template`.`PERMISSIONS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`PERMISSIONS` ;

CREATE TABLE IF NOT EXISTS `template`.`PERMISSIONS` (
  `PRMS_ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `PRMS_NAME` VARCHAR(64) NOT NULL,
  `PRMS_DESCRIPTION` TEXT NULL,
  `PRMS_ACTIVE` TINYINT NOT NULL,
  `PRMS_CODE` VARCHAR(8) NOT NULL,
  `PRMS_MENU_ID` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`PRMS_ID`),
  UNIQUE INDEX `PRMS_ID_UNIQUE` (`PRMS_ID` ASC),
  UNIQUE INDEX `PRMS_NAME_UNIQUE` (`PRMS_NAME` ASC),
  UNIQUE INDEX `PRMS_CODE_UNIQUE` (`PRMS_CODE` ASC),
  INDEX `FK_PRMS_MENU_ID_idx` (`PRMS_MENU_ID` ASC),
  CONSTRAINT `FK_PRMS_MENU_ID`
    FOREIGN KEY (`PRMS_MENU_ID`)
    REFERENCES `template`.`MENU_ITEMS` (`MENU_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `template`.`ROLE_PERMISSION_MAPPINGS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`ROLE_PERMISSION_MAPPINGS` ;

CREATE TABLE IF NOT EXISTS `template`.`ROLE_PERMISSION_MAPPINGS` (
  `RPMP_ROLE_ID` BIGINT UNSIGNED NOT NULL,
  `RPMP_PRMS_ID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`RPMP_ROLE_ID`, `RPMP_PRMS_ID`),
  INDEX `fk_RPMP_PRMS_idx` (`RPMP_PRMS_ID` ASC),
  INDEX `fk_RPMP_ROLE_idx` (`RPMP_ROLE_ID` ASC),
  CONSTRAINT `fk_RPMP_ROLE`
    FOREIGN KEY (`RPMP_ROLE_ID`)
    REFERENCES `template`.`ROLES` (`ROLE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RPMP_PRMS`
    FOREIGN KEY (`RPMP_PRMS_ID`)
    REFERENCES `template`.`PERMISSIONS` (`PRMS_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `template`.`LOGS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`LOGS` ;

CREATE TABLE IF NOT EXISTS `template`.`LOGS` (
  `LOG_ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `LOG_USERNAME` VARCHAR(127) NOT NULL,
  `LOG_CONTENT` TEXT NOT NULL,
  `LOG_CREATED_TIME` TIMESTAMP NOT NULL,
  `LOG_ACTIVE` TINYINT NOT NULL,
  PRIMARY KEY (`LOG_ID`),
  UNIQUE INDEX `LOG_ID_UNIQUE` (`LOG_ID` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `template`.`USER_ROLE_MAPPINGS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`USER_ROLE_MAPPINGS` ;

CREATE TABLE IF NOT EXISTS `template`.`USER_ROLE_MAPPINGS` (
  `URMP_USER_ID` BIGINT UNSIGNED NOT NULL,
  `URMP_ROLE_ID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`URMP_USER_ID`, `URMP_ROLE_ID`),
  INDEX `FK_ROLE_idx` (`URMP_ROLE_ID` ASC),
  CONSTRAINT `FK_URMP_USER`
    FOREIGN KEY (`URMP_USER_ID`)
    REFERENCES `template`.`USERS` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_URMP_ROLE`
    FOREIGN KEY (`URMP_ROLE_ID`)
    REFERENCES `template`.`ROLES` (`ROLE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `template`.`USER_PERMISSION_MAPPINGS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`.`USER_PERMISSION_MAPPINGS` ;

CREATE TABLE IF NOT EXISTS `template`.`USER_PERMISSION_MAPPINGS` (
  `UPMP_USER_ID` BIGINT UNSIGNED NOT NULL,
  `UPMP_PRMS_ID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`UPMP_USER_ID`, `UPMP_PRMS_ID`),
  INDEX `FK_URMP_ROLE_idx` (`UPMP_PRMS_ID` ASC),
  CONSTRAINT `FK_UPMP_USER`
    FOREIGN KEY (`UPMP_USER_ID`)
    REFERENCES `template`.`USERS` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_UPMP_PRMS`
    FOREIGN KEY (`UPMP_PRMS_ID`)
    REFERENCES `template`.`PERMISSIONS` (`PRMS_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- -----------------------------------------------------
-- Data for table `template`.`USER_STATUSES`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`USER_STATUSES` (`USST_ID`, `USST_NAME`, `USST_DESCRIPTION`, `USST_ACTIVE`, `USST_CODE`) VALUES (1, 'Normal', 'Normal', 1, '100');
INSERT INTO `template`.`USER_STATUSES` (`USST_ID`, `USST_NAME`, `USST_DESCRIPTION`, `USST_ACTIVE`, `USST_CODE`) VALUES (2, 'Deactived', 'Deactived', 1, '400');
INSERT INTO `template`.`USER_STATUSES` (`USST_ID`, `USST_NAME`, `USST_DESCRIPTION`, `USST_ACTIVE`, `USST_CODE`) VALUES (3, 'Frozen', 'Frozen', 1, '501');

COMMIT;


-- -----------------------------------------------------
-- Data for table `template`.`USERS`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`USERS` (`USER_ID`, `USER_USERNAME`, `USER_PASSWORD`, `USER_FIRST_NAME`, `USER_LAST_NAME`, `USER_EMAIL`, `USER_PROFILE_IMAGE`, `USER_REMARK`, `USER_CREATED_TIME`, `USER_UPDATED_TIME`, `USER_ACTIVE`, `USER_USST_ID`) VALUES (1, 'admin', '$2a$10$Ce2HJja0Trha0ee3.rMqQewIzJMVe87.jNi5zF5gDdsyvHjJsnwOm', 'Admin', 'Admin', 'ironaire@gmail.com', NULL, NULL, now(), NULL, 1, 1);
INSERT INTO `template`.`USERS` (`USER_ID`, `USER_USERNAME`, `USER_PASSWORD`, `USER_FIRST_NAME`, `USER_LAST_NAME`, `USER_EMAIL`, `USER_PROFILE_IMAGE`, `USER_REMARK`, `USER_CREATED_TIME`, `USER_UPDATED_TIME`, `USER_ACTIVE`, `USER_USST_ID`) VALUES (2, 'user', '$2a$10$Ce2HJja0Trha0ee3.rMqQewIzJMVe87.jNi5zF5gDdsyvHjJsnwOm', 'User', 'User', 'userironaire@gmail.com', NULL, NULL, now(), NULL, 1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `template`.`ROLES`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`ROLES` (`ROLE_ID`, `ROLE_NAME`, `ROLE_DESCRIPTION`, `ROLE_ACTIVE`, `ROLE_CODE`) VALUES (1, 'ROLE_ADMIN', 'System Admin', 1, '100');
INSERT INTO `template`.`ROLES` (`ROLE_ID`, `ROLE_NAME`, `ROLE_DESCRIPTION`, `ROLE_ACTIVE`, `ROLE_CODE`) VALUES (2, 'ROLE_USER', 'Merchant Admin', 1, '200');

COMMIT;


-- -----------------------------------------------------
-- Data for table `template`.`MENU_CATEGORIES`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`MENU_CATEGORIES` (`MNCG_ID`, `MNCG_NAME`, `MNCG_INDEX`) VALUES (1, 'userManagement', 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `template`.`MENU_ITEMS`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`MENU_ITEMS` (`MENU_ID`, `MENU_NAME`, `MENU_INDEX`, `MENU_TARGET`, `MENU_MNCG_ID`) VALUES (1, 'manageUser', 11, 'user/index/all', 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `template`.`PERMISSIONS`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`PERMISSIONS` (`PRMS_ID`, `PRMS_NAME`, `PRMS_DESCRIPTION`, `PRMS_ACTIVE`, `PRMS_CODE`, `PRMS_MENU_ID`) VALUES (1, 'manageUser', NULL, 1, '000', 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `template`.`ROLE_PERMISSION_MAPPINGS`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`ROLE_PERMISSION_MAPPINGS` (`RPMP_ROLE_ID`, `RPMP_PRMS_ID`) VALUES (1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `template`.`USER_ROLE_MAPPINGS`
-- -----------------------------------------------------
START TRANSACTION;
USE `template`;
INSERT INTO `template`.`USER_ROLE_MAPPINGS` (`URMP_USER_ID`, `URMP_ROLE_ID`) VALUES (1, 1);

COMMIT;

