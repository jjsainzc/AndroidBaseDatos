/*
SQLyog Ultimate v9.50 
MySQL - 5.5.5-10.0.34-MariaDB : Database - base_datos
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`base_datos` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `base_datos`;

/*Table structure for table `persona` */

DROP TABLE IF EXISTS `persona`;

CREATE TABLE `persona` (
  `persona_id` int(16) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `cedula` varchar(13) NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `estado_civil` varchar(50) DEFAULT NULL,
  `genero` varchar(50) DEFAULT NULL,
  `estatura` double(20,6) DEFAULT NULL,
  PRIMARY KEY (`persona_id`),
  UNIQUE KEY `cedula_idx` (`cedula`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `persona` */

insert  into `persona`(`persona_id`,`nombre`,`cedula`,`fecha_nacimiento`,`estado_civil`,`genero`,`estatura`) values (1,'Jorge ','1762534785','1964-06-30','Casado','Masculino',1.500000),(2,'Juan','1524367892','1980-09-06','Viudo','Masculino',1.600000),(3,'Rosa','1725434567','1978-05-12','Soltero','Femenino',1.400000),(4,'Jose','1786545678','1990-12-09','Soltero','Masculino',1.500000);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
