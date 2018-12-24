# ************************************************************
# Sequel Pro SQL dump
# Version 5425
#
# https://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 8.0.13)
# Database: bank
# Generation Time: 2018-12-24 09:31:29 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Admin`;

CREATE TABLE `Admin` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `salt` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `Admin` WRITE;
/*!40000 ALTER TABLE `Admin` DISABLE KEYS */;

INSERT INTO `Admin` (`id`, `account`, `password`, `salt`)
VALUES
	(1,'root','fDhLgvlRr6PYlff2kecwMC2ADdRLlwvds8horN5EYAg=','P2EKEKgfKFr3rPEfEE3E'),
	(2,'operator','10yNLzv0AD5eLUIIhRuVQpKZvDdk6O/7uxY+Z8gTDK4=','ggWO2EF4EOr3WKPUrKgO'),
	(3,'manager','xrLrZRKyJaCJK9NRgj4PQxgr2DElhujseaR8oQK8XAw=','OK4KKFPFFFFEFEPFFFPr');

/*!40000 ALTER TABLE `Admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AdminRole
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AdminRole`;

CREATE TABLE `AdminRole` (
  `roleId` int(11) unsigned NOT NULL,
  `adminId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`roleId`,`adminId`),
  KEY `adminId` (`adminId`),
  CONSTRAINT `AdminRole_ibfk_1` FOREIGN KEY (`adminId`) REFERENCES `Admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `AdminRole` WRITE;
/*!40000 ALTER TABLE `AdminRole` DISABLE KEYS */;

INSERT INTO `AdminRole` (`roleId`, `adminId`)
VALUES
	(2,1),
	(4,2),
	(1,3),
	(2,3),
	(3,3);

/*!40000 ALTER TABLE `AdminRole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Role`;

CREATE TABLE `Role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;

INSERT INTO `Role` (`id`, `description`)
VALUES
	(1,'transfer user money'),
	(2,'view transactionLog'),
	(3,'credit or debit user money'),
	(4,'super admin');

/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Transaction
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Transaction`;

CREATE TABLE `Transaction` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fromUserId` int(11) unsigned NOT NULL,
  `ToUserId` int(11) unsigned DEFAULT NULL,
  `action` tinyint(2) NOT NULL,
  `adminId` int(11) unsigned DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `adminId` (`adminId`),
  KEY `fromUserId` (`fromUserId`),
  KEY `ToUserId` (`ToUserId`),
  CONSTRAINT `Transaction_ibfk_1` FOREIGN KEY (`adminId`) REFERENCES `Admin` (`id`),
  CONSTRAINT `Transaction_ibfk_2` FOREIGN KEY (`fromUserId`) REFERENCES `User` (`id`),
  CONSTRAINT `Transaction_ibfk_3` FOREIGN KEY (`ToUserId`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table User
# ------------------------------------------------------------

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `wallet` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;

INSERT INTO `User` (`id`, `wallet`)
VALUES
	(1,10000),
	(2,10000);

/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
