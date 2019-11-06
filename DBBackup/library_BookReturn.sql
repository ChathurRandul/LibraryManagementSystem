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
-- Table structure for table `BookReturn`
--

DROP TABLE IF EXISTS `BookReturn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BookReturn` (
  `bookid` varchar(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `issuedate` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `issueid` varchar(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `booktitle` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `memberid` varchar(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `membername` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `returndate` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fine` double(7,2) DEFAULT NULL,
  KEY `issueid` (`issueid`),
  KEY `memberid` (`memberid`),
  KEY `bookid` (`bookid`),
  CONSTRAINT `BookReturn_ibfk_1` FOREIGN KEY (`issueid`) REFERENCES `BookIssue` (`issueid`),
  CONSTRAINT `BookReturn_ibfk_2` FOREIGN KEY (`memberid`) REFERENCES `LibraryMember` (`memberid`),
  CONSTRAINT `BookReturn_ibfk_3` FOREIGN KEY (`bookid`) REFERENCES `Book` (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BookReturn`
--

LOCK TABLES `BookReturn` WRITE;
/*!40000 ALTER TABLE `BookReturn` DISABLE KEYS */;
INSERT INTO `BookReturn` VALUES ('BK002','2019-09-12','IB006','Human Biology','MBR001','Charith De Silva','2019-09-12',0.00);
/*!40000 ALTER TABLE `BookReturn` ENABLE KEYS */;
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
