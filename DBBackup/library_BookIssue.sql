-- MySQL dump 10.13  Distrib 8.0.17, for Linux (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BookIssue`
--

DROP TABLE IF EXISTS `BookIssue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BookIssue` (
  `issuedate` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `issueid` varchar(7) COLLATE utf8mb4_unicode_ci NOT NULL,
  `memberid` varchar(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `membername` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bookid` varchar(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `booktitle` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `returnstatus` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`issueid`),
  KEY `memberid` (`memberid`),
  KEY `bookid` (`bookid`),
  CONSTRAINT `BookIssue_ibfk_1` FOREIGN KEY (`memberid`) REFERENCES `LibraryMember` (`memberid`),
  CONSTRAINT `BookIssue_ibfk_2` FOREIGN KEY (`bookid`) REFERENCES `Book` (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BookIssue`
--

LOCK TABLES `BookIssue` WRITE;
/*!40000 ALTER TABLE `BookIssue` DISABLE KEYS */;
INSERT INTO `BookIssue` VALUES ('2019-09-11','IB002','MBR002','Suvin Perera','BK003','CheckMate - Ultimate Guide','Returned'),('2019-09-11','IB003','MBR004','Gayan Pathirana','BK005','Famous Five: Mystery of Devil\'s Rock','Pending'),('2019-09-12','IB004','MBR005','Sumudu Upatissa','BK004','Nightflyers','Pending'),('2019-09-12','IB005','MBR006','Eranga Rajamanthri','BK001','Robin Hood','Pending'),('2019-09-12','IB006','MBR001','Charith De Silva','BK002','Human Biology','Returned');
/*!40000 ALTER TABLE `BookIssue` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-06 11:14:35
