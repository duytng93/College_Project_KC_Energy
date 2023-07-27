-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: kc_energy
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `accountNumber` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `phoneNumber` varchar(45) NOT NULL,
  `plan` varchar(45) NOT NULL,
  `remainingDue` decimal(10,2) DEFAULT NULL,
  `lastPayment` decimal(10,2) DEFAULT NULL,
  `lastPaymentDate` date DEFAULT NULL,
  `currentUsage` decimal(10,2) DEFAULT NULL,
  `lastUsage` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`accountNumber`),
  UNIQUE KEY `customerID_UNIQUE` (`accountNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=1023 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1000,'James Smith','1202 NW 72nd Terrace, Kansas City, MO 64158','816-223-3342','resident',0.00,96.35,'2023-03-15',396.20,385.40),(1001,'Michael Jordan','1220 NW 74th St, Kansas City, MO 64134','816-663-3835','resident',3.19,1.00,'2023-04-25',696.20,697.40),(1002,'Phu Ly','6559 N Oak Trafficway, Gladstone, MO 64119','816-492-6501','business',0.00,577.40,'2023-03-17',1500.00,1452.00),(1003,'Maria Garcia','1220 NW 74th St, Kansas City, MO 64120','816-663-3792','business',0.00,576.50,'2023-03-20',1457.00,1450.00),(1004,'James Smith','421 Gladstone Blvd, Kansas City, MO 64124','816-533-6548','resident',0.00,159.92,'2023-03-10',654.00,632.00),(1005,'John Williams','3513 Thompson Ave, Kansas City, MO 64124','816-564-6597','resident',133.75,125.00,'2023-03-10',0.00,535.00),(1006,'Robert Lee Lo','641 Cleveland Ave., Kansas City, MO 64124','913-568-6432','resident',173.75,15.00,'2023-04-28',0.00,725.00),(1007,'Michael Lee','4019 Independence Ave, Kansas City, MO 64124','816-483-4278','business',0.00,579.65,'2023-03-02',1375.50,1457.00),(1008,'William Moore','4618 Independence Ave, Kansas City, MO 64124','816-813-0781','business',0.00,932.22,'2023-03-02',2104.50,2240.50),(1009,'David Ramirez','637 Brighton Ave, Kansas City, MO 64124','816-987-4513','resident',0.00,97.13,'2023-03-15',375.50,388.50),(1010,'Charles Lee Ki','211 N Mersington Ave, Kansas City, MO 64123','816-785-5421','resident',35.50,200.00,'2023-03-19',752.12,791.50),(1011,'Joseph Smith','3920 Windsor Ave, Kansas City, MO 64123','816-377-4173','business',0.00,529.48,'2023-03-21',1253.20,1345.50),(1012,'Thomas Jackson','217 Gladstone Blvd, Kansas City, MO 64123','816-214-4513','resident',0.00,146.35,'2023-03-05',572.20,585.50),(1013,'Christopher','1010 E 18th Ave, North Kansas City, MO 64116','816-221-8292','business',0.00,637.48,'2023-03-05',1572.20,1585.50),(1014,'Daniel Thomas','829 E 21st Ave, North Kansas City, MO 64116','816-451-4456','resident',22.45,100.00,'2023-03-05',486.50,489.80),(1015,'Paul Jackson','828 E 22nd Ave, North Kansas City, MO 64116','913-456-7894','resident',0.00,162.46,'2023-03-17',645.20,640.20),(1016,'Mark Clark','4005 N Oak Trafficway, Kansas City, MO 64116','816-832-8484','business',0.00,1112.09,'2023-03-17',2645.20,2640.20),(1017,'James Miller','4107 N Cherry St s. c, Kansas City, MO 64116','816-462-8252','business',0.00,1022.09,'2023-03-17',2345.20,2440.20),(1018,'John Moore','624 NE 41st St, Kansas City, MO 64116','816-451-4547','resident',0.00,0.00,'1900-01-01',0.00,0.00),(1019,'Robert Thomas','4233 N Grand Ave, Kansas City, MO 64116','913-456-8522','resident',0.00,0.00,'1900-01-01',0.00,0.00),(1020,'William Smith','20 NW 43 Terrace, Kansas City, MO 64116','913-456-7845','resident',0.00,0.00,'1900-01-01',0.00,0.00),(1021,'David Clark','1209 NW Lakeview Ave, Kansas City, MO 64118','913-745-5621','resident',0.00,0.00,'1900-01-01',0.00,0.00),(1022,'Richard Miller','5722 N Jarboe St, Kansas City, MO 64118','913-458-5612','resident',0.00,0.00,'1900-01-01',0.00,0.00);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-28 19:27:03
