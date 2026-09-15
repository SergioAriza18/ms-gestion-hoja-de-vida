-- ============================================================================
-- Estructura completa de la base de datos compartida
-- No contiene registros. Recrea las tablas del esquema maestriacomputacion_HV.
-- ============================================================================
CREATE DATABASE IF NOT EXISTS `maestriacomputacion_HV` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `maestriacomputacion_HV`;

-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: maestriacomputacion_HV
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `actas`
--

DROP TABLE IF EXISTS `actas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_actas` date DEFAULT NULL,
  `numero_actas` bigint DEFAULT NULL,
  `id_doc_maestria` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_documento_acta` (`id_doc_maestria`) USING BTREE,
  CONSTRAINT `fk_documento_acta` FOREIGN KEY (`id_doc_maestria`) REFERENCES `documentos_maestria` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `actas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `actas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `actas_asignaturas`
--

DROP TABLE IF EXISTS `actas_asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actas_asignaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_acta_asignatura` bit(1) DEFAULT NULL,
  `id_acta` bigint DEFAULT NULL,
  `id_asignatura` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_asignaturas` (`id_asignatura`) USING BTREE,
  KEY `fk_actas` (`id_acta`) USING BTREE,
  CONSTRAINT `fk_actas` FOREIGN KEY (`id_acta`) REFERENCES `actas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_asignaturas` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `actividades`
--

DROP TABLE IF EXISTS `actividades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actividades` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idpractica` bigint NOT NULL,
  `nombreact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tipoact` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `soporteact` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `horasact` int DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_practica_actividad` (`idpractica`) USING BTREE,
  CONSTRAINT `fk_practica_actividad` FOREIGN KEY (`idpractica`) REFERENCES `practicas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `actividades_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `actividades_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `actividades_realizadas_practica_docente`
--

DROP TABLE IF EXISTS `actividades_realizadas_practica_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actividades_realizadas_practica_docente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `id_sub_tipo_solicitud` bigint NOT NULL,
  `intensidad_horaria` int DEFAULT NULL,
  `horas_reconocer` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_act_realizadas_pd_solicitud` (`id_solicitud`) USING BTREE,
  KEY `fk_act_realizadas_pd_subtipos` (`id_sub_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_act_realizadas_pd_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_act_realizadas_pd_subtipos` FOREIGN KEY (`id_sub_tipo_solicitud`) REFERENCES `sub_tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `adicionar_asignaturas`
--

DROP TABLE IF EXISTS `adicionar_asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adicionar_asignaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_adicionar_asignaturas` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_adicionar_asignaturas` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anexos_respuesta_examen_valoracion`
--

DROP TABLE IF EXISTS `anexos_respuesta_examen_valoracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anexos_respuesta_examen_valoracion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `link_anexo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_respuesta_examen_valoracion` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_respuesta_examen_valoracion_anexos` (`id_respuesta_examen_valoracion`) USING BTREE,
  CONSTRAINT `fk_respuesta_examen_valoracion_anexos` FOREIGN KEY (`id_respuesta_examen_valoracion`) REFERENCES `respuestas_examen_valoracion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anexos_solicitud_examen_valoracion`
--

DROP TABLE IF EXISTS `anexos_solicitud_examen_valoracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anexos_solicitud_examen_valoracion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `link_anexo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_examen_valoracion` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_examen_valoracion_anexos` (`id_examen_valoracion`) USING BTREE,
  CONSTRAINT `fk_solicitud_examen_valoracion_anexos` FOREIGN KEY (`id_examen_valoracion`) REFERENCES `solicitudes_examen_valoracion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anexos_sustentacion`
--

DROP TABLE IF EXISTS `anexos_sustentacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anexos_sustentacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `link_anexo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_sustentacion_proyecto_investigacion` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_sustentacion_anexos` (`id_sustentacion_proyecto_investigacion`) USING BTREE,
  CONSTRAINT `fk_sustentacion_anexos` FOREIGN KEY (`id_sustentacion_proyecto_investigacion`) REFERENCES `sustentaciones_proyecto_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aplazar_semestre`
--

DROP TABLE IF EXISTS `aplazar_semestre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aplazar_semestre` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `semestre` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `motivo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_aplazar_semestre` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_aplazar_semestre` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `apoyos_economicos_congresos`
--

DROP TABLE IF EXISTS `apoyos_economicos_congresos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apoyos_economicos_congresos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `nombre_congreso` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tipo_congreso` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `director_grupo_inv` bigint NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `titulo_publicacion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `valor_apoyo` bigint DEFAULT NULL,
  `entidad_bancaria` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_cuenta` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_cuenta` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_cedula_asociada` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `direccion_residencia` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_apoyo_economico_cong` (`id_solicitud`) USING BTREE,
  KEY `fk_director_grupo_apoyo_economico_cong` (`director_grupo_inv`) USING BTREE,
  CONSTRAINT `fk_director_grupo_apoyo_economico_cong` FOREIGN KEY (`director_grupo_inv`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solicitud_apoyo_economico_cong` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `apoyos_economicos_investigacion`
--

DROP TABLE IF EXISTS `apoyos_economicos_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apoyos_economicos_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `lugar_pasantia` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `director_grupo_inv` bigint NOT NULL,
  `grupo_investigacion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `valor_apoyo` bigint DEFAULT NULL,
  `entidad_bancaria` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_cuenta` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_cuenta` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_cedula_asociada` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `direccion_residencia` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_apoyo_economico` (`id_solicitud`) USING BTREE,
  KEY `fk_director_grupo_apoyo_economico` (`director_grupo_inv`) USING BTREE,
  CONSTRAINT `fk_director_grupo_apoyo_economico` FOREIGN KEY (`director_grupo_inv`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solicitud_apoyo_economico` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `apoyos_economicos_pago_publicacion_evento`
--

DROP TABLE IF EXISTS `apoyos_economicos_pago_publicacion_evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apoyos_economicos_pago_publicacion_evento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `nombre_evento` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tipo_evento` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `director_grupo_inv` bigint NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `titulo_publicacion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `valor_apoyo` bigint DEFAULT NULL,
  `entidad_bancaria` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_cuenta` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_cuenta` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_cedula_asociada` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `direccion_residencia` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_apoyo_economico_pago` (`id_solicitud`) USING BTREE,
  KEY `fk_director_grupo_apoyo_economico_pago` (`director_grupo_inv`) USING BTREE,
  CONSTRAINT `fk_director_grupo_apoyo_economico_pago` FOREIGN KEY (`director_grupo_inv`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solicitud_apoyo_economico_pago` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `areas_formacion`
--

DROP TABLE IF EXISTS `areas_formacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `areas_formacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asignaturas`
--

DROP TABLE IF EXISTS `asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo_asignatura` varchar(30) DEFAULT NULL,
  `contenido_asignatura` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `creditos` int DEFAULT NULL,
  `estado_asignatura` bit(1) DEFAULT NULL,
  `fecha_aprobacion` datetime(6) DEFAULT NULL,
  `horas_no_presencial` int DEFAULT NULL,
  `horas_presencial` int DEFAULT NULL,
  `horas_total` int DEFAULT NULL,
  `nombre_asignatura` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `objetivo_asignatura` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_asignatura` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `area_formacion` bigint DEFAULT NULL,
  `contenido_programatico` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `lineas_investigacion` bigint DEFAULT NULL,
  `microcurriculo` bigint DEFAULT NULL,
  `oficio_facultad` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_asignaturas_codigo_asignatura` (`codigo_asignatura`),
  KEY `fk_microcurriculo` (`microcurriculo`) USING BTREE,
  KEY `fk_oficio_facultad` (`oficio_facultad`) USING BTREE,
  KEY `asignaturas_areas_formacion_FK` (`area_formacion`) USING BTREE,
  CONSTRAINT `asignaturas_areas_formacion_FK` FOREIGN KEY (`area_formacion`) REFERENCES `areas_formacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_microcurriculo` FOREIGN KEY (`microcurriculo`) REFERENCES `otros_documentos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_oficio_facultad` FOREIGN KEY (`oficio_facultad`) REFERENCES `oficios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `asignaturas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `asignaturas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asignaturas_adicionadas`
--

DROP TABLE IF EXISTS `asignaturas_adicionadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaturas_adicionadas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_adicionar_asignatura` bigint NOT NULL,
  `id_asignatura` bigint DEFAULT NULL,
  `noombre_asignatura` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_docente` bigint NOT NULL,
  `estado` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_asignaturas_adicionadas` (`id_adicionar_asignatura`) USING BTREE,
  KEY `fk_docente_asig_adicionadas` (`id_docente`) USING BTREE,
  CONSTRAINT `fk_asignaturas_adicionadas` FOREIGN KEY (`id_adicionar_asignatura`) REFERENCES `adicionar_asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_docente_asig_adicionadas` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asignaturas_canceladas`
--

DROP TABLE IF EXISTS `asignaturas_canceladas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaturas_canceladas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_cancelar_asignatura` bigint NOT NULL,
  `id_asignatura` bigint DEFAULT NULL,
  `noombre_asignatura` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `grupo` varchar(100) DEFAULT NULL,
  `id_docente` bigint NOT NULL,
  `estado` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `aprobado_comite` tinyint(1) NOT NULL DEFAULT '0',
  `aprobado_concejo` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_cancelar_asignatura` (`id_cancelar_asignatura`) USING BTREE,
  KEY `fk_docente_asig_canceladas` (`id_docente`) USING BTREE,
  CONSTRAINT `fk_cancelar_asignatura` FOREIGN KEY (`id_cancelar_asignatura`) REFERENCES `cancelar_asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_docente_asig_canceladas` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asignaturas_externas`
--

DROP TABLE IF EXISTS `asignaturas_externas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaturas_externas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `programa_procedencia` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `institucion_procedencia` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `numero_creditos` int NOT NULL,
  `intensidad_horaria` int NOT NULL,
  `contenido_programatico` mediumtext,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `asignaturas_externas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `asignaturas_externas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asignaturas_homologadas`
--

DROP TABLE IF EXISTS `asignaturas_homologadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaturas_homologadas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_homologacion` bigint NOT NULL,
  `id_asignatura_homologar` bigint DEFAULT NULL,
  `id_asignatura_externa` bigint NOT NULL,
  `calificacion_obtenida` decimal(2,1) NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `aprobado_comite` tinyint(1) NOT NULL DEFAULT '0',
  `aprobado_concejo` tinyint(1) NOT NULL DEFAULT '0',
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_homologacion_asignaturas_homologadas` (`id_homologacion`) USING BTREE,
  KEY `fk_asig_homologar_asignaturas_homologadas` (`id_asignatura_homologar`) USING BTREE,
  KEY `fk_asig_externa_asignaturas_homologadas` (`id_asignatura_externa`) USING BTREE,
  CONSTRAINT `fk_asig_externa_asignaturas_homologadas` FOREIGN KEY (`id_asignatura_externa`) REFERENCES `asignaturas_externas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_asig_homologar_asignaturas_homologadas` FOREIGN KEY (`id_asignatura_homologar`) REFERENCES `asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_homologacion_asignaturas_homologadas` FOREIGN KEY (`id_homologacion`) REFERENCES `homologaciones` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `asignaturas_homologadas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `asignaturas_homologadas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aval_comite_programa`
--

DROP TABLE IF EXISTS `aval_comite_programa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aval_comite_programa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `id_sub_tipo_solicitud` bigint NOT NULL,
  `intensidad_horaria` int NOT NULL,
  `horas_reconocer` double NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_aval_comite_solicitud` (`id_solicitud`) USING BTREE,
  KEY `fk_aval_comite_subtipos` (`id_sub_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_aval_comite_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_aval_comite_subtipos` FOREIGN KEY (`id_sub_tipo_solicitud`) REFERENCES `sub_tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aval_seminario_actualizacion`
--

DROP TABLE IF EXISTS `aval_seminario_actualizacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aval_seminario_actualizacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_aval_seminario_act` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_aval_seminario_act` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `avales_pasantia_investigacion`
--

DROP TABLE IF EXISTS `avales_pasantia_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `avales_pasantia_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `lugar_pasantia` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_avales_pas` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_avales_pas` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `becas`
--

DROP TABLE IF EXISTS `becas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `becas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dedicacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `entidad_asociada` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `es_ofrecida_por_unicauca` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `titulo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_estudiante` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_beca` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante_beca` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `becas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `becas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cancelar_asignaturas`
--

DROP TABLE IF EXISTS `cancelar_asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cancelar_asignaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `motivo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `documento_adjunto` mediumtext,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_cancelar_asignaturas` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_cancelar_asignaturas` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categorias_lineas_investigacion`
--

DROP TABLE IF EXISTS `categorias_lineas_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias_lineas_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `certificados_votos`
--

DROP TABLE IF EXISTS `certificados_votos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificados_votos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_estudiante` bigint NOT NULL,
  `fechacert` date NOT NULL,
  `enlacecert` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_certificado` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante_certificado` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `certificados_votos_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `certificados_votos_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coordinadores`
--

DROP TABLE IF EXISTS `coordinadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coordinadores` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_persona` bigint NOT NULL,
  `id_usuario` bigint NOT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_persona_coordinador` (`id_persona`) USING BTREE,
  KEY `fk_usuario_coordinador` (`id_usuario`) USING BTREE,
  CONSTRAINT `fk_persona_coordinador` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_usuario_coordinador` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `coordinadores_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `coordinadores_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cuestionarios`
--

DROP TABLE IF EXISTS `cuestionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuestionarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `observacion` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVO',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cuestionarios_falid`
--

DROP TABLE IF EXISTS `cuestionarios_falid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuestionarios_falid` (
  `idevaldocente` bigint NOT NULL,
  `idcuestionario` bigint NOT NULL,
  `nombrecuestionario` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `observacioncuest` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fechacreacioncuest` date DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idevaldocente`,`idcuestionario`) USING BTREE,
  CONSTRAINT `fk_evaluacion_cuestionario` FOREIGN KEY (`idevaldocente`) REFERENCES `evaluaciones_docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cuestionarios_falid_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `cuestionarios_falid_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cuestionarios_preguntas`
--

DROP TABLE IF EXISTS `cuestionarios_preguntas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuestionarios_preguntas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idevaldocente` bigint NOT NULL,
  `idcuestionario` bigint NOT NULL,
  PRIMARY KEY (`id`,`idevaldocente`,`idcuestionario`) USING BTREE,
  KEY `fk_cuestionario_preguntas2` (`idevaldocente`,`idcuestionario`) USING BTREE,
  CONSTRAINT `fk_cuestionario_preguntas2` FOREIGN KEY (`idevaldocente`, `idcuestionario`) REFERENCES `cuestionarios_falid` (`idevaldocente`, `idcuestionario`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cursar_asignaturas`
--

DROP TABLE IF EXISTS `cursar_asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursar_asignaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `motivo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_cursar_asignaturas` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_cursar_asignaturas` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `curso_docente`
--

DROP TABLE IF EXISTS `curso_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curso_docente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_curso` bigint NOT NULL,
  `id_docente` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `curso_docente_cursos_FK` (`id_curso`) USING BTREE,
  KEY `curso_docente_docentes_FK` (`id_docente`) USING BTREE,
  CONSTRAINT `curso_docente_cursos_FK` FOREIGN KEY (`id_curso`) REFERENCES `cursos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `curso_docente_docentes_FK` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `curso_material`
--

DROP TABLE IF EXISTS `curso_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curso_material` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_material` bigint NOT NULL,
  `id_curso` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `curso_material_cursos_FK` (`id_curso`) USING BTREE,
  KEY `curso_material_materiales_apoyo_FK` (`id_material`) USING BTREE,
  CONSTRAINT `curso_material_cursos_FK` FOREIGN KEY (`id_curso`) REFERENCES `cursos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `curso_material_materiales_apoyo_FK` FOREIGN KEY (`id_material`) REFERENCES `materiales_apoyo` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cursos`
--

DROP TABLE IF EXISTS `cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `grupocurso` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `periodocurso` int DEFAULT NULL,
  `aniocurso` int DEFAULT NULL,
  `horariocurso` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `saloncurso` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_asignatura` bigint NOT NULL,
  `observacioncurso` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `periodo_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `cursos_asignaturas_FK` (`id_asignatura`) USING BTREE,
  KEY `cursos_periodo_academico_FK` (`periodo_id`) USING BTREE,
  CONSTRAINT `cursos_asignaturas_FK` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cursos_periodo_academico_FK` FOREIGN KEY (`periodo_id`) REFERENCES `periodo_academico` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cursos_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `cursos_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cursos_dictados`
--

DROP TABLE IF EXISTS `cursos_dictados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos_dictados` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_fin` date NOT NULL,
  `fecha_inicio` date NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `orientadoa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_estudiante` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_curso` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante_curso` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `datos_cursar_asignatura`
--

DROP TABLE IF EXISTS `datos_cursar_asignatura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datos_cursar_asignatura` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_cursar_asignatura` bigint NOT NULL,
  `id_asignatura_externa` bigint NOT NULL,
  `codigo_asignatura` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `grupo` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nombre_docente` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `titulo_docente` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `carta_aceptacion` mediumtext,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_cursar_asignatura_datos` (`id_cursar_asignatura`) USING BTREE,
  KEY `fk_asig_externa_datos` (`id_asignatura_externa`) USING BTREE,
  CONSTRAINT `fk_asig_externa_datos` FOREIGN KEY (`id_asignatura_externa`) REFERENCES `asignaturas_externas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_cursar_asignatura_datos` FOREIGN KEY (`id_cursar_asignatura`) REFERENCES `cursar_asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `descuentos`
--

DROP TABLE IF EXISTS `descuentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `descuentos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` bigint NOT NULL,
  `fechainiciodes` date NOT NULL,
  `fechafindes` date NOT NULL,
  `tipodes` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `porcentajedes` int NOT NULL,
  `numactades` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fechaactades` date DEFAULT NULL,
  `numresoldes` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `resoluciondes` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `poliza` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_descuento` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante_descuento` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `descuentos_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `descuentos_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distinciones_academicas`
--

DROP TABLE IF EXISTS `distinciones_academicas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `distinciones_academicas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_distincion_academica_codigo` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `docente_estudiante`
--

DROP TABLE IF EXISTS `docente_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docente_estudiante` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_docente` bigint DEFAULT NULL,
  `id_estudiante` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UKc60j441b4w4qkctf9l2mq8ppy` (`id_docente`,`id_estudiante`) USING BTREE,
  KEY `FK50wngq9c87vl3nyelg5yyowls` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_doc_est_id_docente` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_doc_est_id_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `docentes`
--

DROP TABLE IF EXISTS `docentes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docentes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `departamento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `escalafon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `facultad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `observacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_vinculacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_persona` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_persona_docente` (`id_persona`) USING BTREE,
  CONSTRAINT `fk_persona_docente` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `docentes_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `docentes_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `docentes_asignatura`
--

DROP TABLE IF EXISTS `docentes_asignatura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docentes_asignatura` (
  `id` bigint NOT NULL,
  `id_asignatura` bigint DEFAULT NULL,
  `id_docente` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKaxtw0vwrkqfna1jrm0j3gurmg` (`id_asignatura`) USING BTREE,
  KEY `FKam2onlulre2n249e4i0e070rm` (`id_docente`) USING BTREE,
  CONSTRAINT `FKam2onlulre2n249e4i0e070rm` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKaxtw0vwrkqfna1jrm0j3gurmg` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `docentes_asignaturas`
--

DROP TABLE IF EXISTS `docentes_asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docentes_asignaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dicta_asignatura` bit(1) DEFAULT NULL,
  `id_asignatura` bigint DEFAULT NULL,
  `id_docente` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_doc_asignatura` (`id_docente`) USING BTREE,
  KEY `fk_asignatura_docente` (`id_asignatura`) USING BTREE,
  CONSTRAINT `fk_asignatura_docente` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_doc_asignatura` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `docentes_estudiantes`
--

DROP TABLE IF EXISTS `docentes_estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docentes_estudiantes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_docente` bigint DEFAULT NULL,
  `id_estudiante` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_docente_est` (`id_docente`) USING BTREE,
  KEY `fk_estudiante_doc` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_docente_est` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_estudiante_doc` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `docentes_lineas_investigacion`
--

DROP TABLE IF EXISTS `docentes_lineas_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docentes_lineas_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_docente` bigint DEFAULT NULL,
  `id_linea_investigacion` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_docente_linea` (`id_docente`) USING BTREE,
  KEY `fk_linea_docente` (`id_linea_investigacion`) USING BTREE,
  CONSTRAINT `fk_docente_linea` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_linea_docente` FOREIGN KEY (`id_linea_investigacion`) REFERENCES `lineas_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_actividades_realizadas`
--

DROP TABLE IF EXISTS `documentos_actividades_realizadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_actividades_realizadas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_actividad_realizada` bigint NOT NULL,
  `id_sub_tipo_solicitud` bigint NOT NULL,
  `documento_base64` mediumtext,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_doc_act_realizadas_actividades` (`id_actividad_realizada`) USING BTREE,
  KEY `fk_doc_act_realizadas_subtipos` (`id_sub_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_doc_act_realizadas_actividades` FOREIGN KEY (`id_actividad_realizada`) REFERENCES `actividades_realizadas_practica_docente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_doc_act_realizadas_subtipos` FOREIGN KEY (`id_sub_tipo_solicitud`) REFERENCES `sub_tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_adjuntos_homologacion`
--

DROP TABLE IF EXISTS `documentos_adjuntos_homologacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_adjuntos_homologacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_homologacion` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_homologacion_doc_adj_homologacion` (`id_homologacion`) USING BTREE,
  CONSTRAINT `fk_homologacion_doc_adj_homologacion` FOREIGN KEY (`id_homologacion`) REFERENCES `homologaciones` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_aplazar_semestre`
--

DROP TABLE IF EXISTS `documentos_aplazar_semestre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_aplazar_semestre` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_aplazar_semestre` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_aplazar_semestre` (`id_aplazar_semestre`) USING BTREE,
  CONSTRAINT `fk_aplazar_semestre` FOREIGN KEY (`id_aplazar_semestre`) REFERENCES `aplazar_semestre` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_apoyo_econo_cong`
--

DROP TABLE IF EXISTS `documentos_apoyo_econo_cong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_apoyo_econo_cong` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_apoyo_econo_cong` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_apoyo_economico_cong_documentos` (`id_apoyo_econo_cong`) USING BTREE,
  CONSTRAINT `fk_apoyo_economico_cong_documentos` FOREIGN KEY (`id_apoyo_econo_cong`) REFERENCES `apoyos_economicos_congresos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_apoyo_econo_pago_pub_evento`
--

DROP TABLE IF EXISTS `documentos_apoyo_econo_pago_pub_evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_apoyo_econo_pago_pub_evento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_apoyo_econo_pago_pub_evento` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_apoyo_economico`
--

DROP TABLE IF EXISTS `documentos_apoyo_economico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_apoyo_economico` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_apoyo_economico` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_apoyo_economico_documentos` (`id_apoyo_economico`) USING BTREE,
  CONSTRAINT `fk_apoyo_economico_documentos` FOREIGN KEY (`id_apoyo_economico`) REFERENCES `apoyos_economicos_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_aval_pasantia`
--

DROP TABLE IF EXISTS `documentos_aval_pasantia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_aval_pasantia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_aval_pasantia` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_aval_pasantia_documentos` (`id_aval_pasantia`) USING BTREE,
  CONSTRAINT `fk_aval_pasantia_documentos` FOREIGN KEY (`id_aval_pasantia`) REFERENCES `avales_pasantia_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_aval_seminario_act`
--

DROP TABLE IF EXISTS `documentos_aval_seminario_act`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_aval_seminario_act` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_aval_seminario_act` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_aval_seminario_act_documentos` (`id_aval_seminario_act`) USING BTREE,
  CONSTRAINT `fk_aval_seminario_act_documentos` FOREIGN KEY (`id_aval_seminario_act`) REFERENCES `aval_seminario_actualizacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_concejo`
--

DROP TABLE IF EXISTS `documentos_concejo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_concejo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud_concejo` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_doc_concejo_id_sol_con` (`id_solicitud_concejo`),
  CONSTRAINT `fk_doc_concejo_id_sol_con` FOREIGN KEY (`id_solicitud_concejo`) REFERENCES `solicitudes_en_concejo` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_cursar_asignatura`
--

DROP TABLE IF EXISTS `documentos_cursar_asignatura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_cursar_asignatura` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_cursar_asignatura` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_cursar_asignatura_documentos` (`id_cursar_asignatura`) USING BTREE,
  CONSTRAINT `fk_cursar_asignatura_documentos` FOREIGN KEY (`id_cursar_asignatura`) REFERENCES `cursar_asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_maestria`
--

DROP TABLE IF EXISTS `documentos_maestria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_maestria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` bit(1) DEFAULT NULL,
  `link_documento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `documentos_maestria_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `documentos_maestria_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_rec_creditos`
--

DROP TABLE IF EXISTS `documentos_rec_creditos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_rec_creditos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_rec_creditos` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_rec_creditos_documentos` (`id_rec_creditos`) USING BTREE,
  CONSTRAINT `fk_rec_creditos_documentos` FOREIGN KEY (`id_rec_creditos`) REFERENCES `reconocimiento_creditos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_requisitos_solicitud`
--

DROP TABLE IF EXISTS `documentos_requisitos_solicitud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_requisitos_solicitud` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre_documento` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_requisito_solicitud` bigint NOT NULL,
  `adjuntar_documento` tinyint(1) NOT NULL DEFAULT '1',
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `abreviatura_documento` varchar(100) DEFAULT NULL,
  `enlace` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_requisito_solicitud` (`id_requisito_solicitud`) USING BTREE,
  CONSTRAINT `fk_requisito_solicitud` FOREIGN KEY (`id_requisito_solicitud`) REFERENCES `requisitos_solicitud` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `documentos_requisitos_solicitud_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `documentos_requisitos_solicitud_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentos_subtipos`
--

DROP TABLE IF EXISTS `documentos_subtipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos_subtipos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_sub_tipo_solicitud` bigint NOT NULL,
  `documento` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_documentos_subtipos_id_sub_tipo_soli` (`id_sub_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_documentos_subtipos_id_sub_tipo_soli` FOREIGN KEY (`id_sub_tipo_solicitud`) REFERENCES `sub_tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `empresa_estudiantes`
--

DROP TABLE IF EXISTS `empresa_estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresa_estudiantes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cargo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `id_empresa` bigint DEFAULT NULL,
  `id_estudiante` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `empresa_estudiantes_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `empresa_estudiantes_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `empresas`
--

DROP TABLE IF EXISTS `empresas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cargo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `correo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `estado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jefe_directo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telefono` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ubicacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_estudiante` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_empresa` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante_empresa` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enlaces_actividades_realizadas`
--

DROP TABLE IF EXISTS `enlaces_actividades_realizadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enlaces_actividades_realizadas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_actividad_realizada` bigint NOT NULL,
  `id_sub_tipo_solicitud` bigint NOT NULL,
  `enlace` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_enl_act_realizadas_actividades` (`id_actividad_realizada`) USING BTREE,
  KEY `fk_enl_act_realizadas_subtipos` (`id_sub_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_enl_act_realizadas_actividades` FOREIGN KEY (`id_actividad_realizada`) REFERENCES `actividades_realizadas_practica_docente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_enl_act_realizadas_subtipos` FOREIGN KEY (`id_sub_tipo_solicitud`) REFERENCES `sub_tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enlaces_rec_creditos`
--

DROP TABLE IF EXISTS `enlaces_rec_creditos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enlaces_rec_creditos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_rec_creditos` bigint NOT NULL,
  `documento` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enlaces_solicitudes`
--

DROP TABLE IF EXISTS `enlaces_solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enlaces_solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `titulo` bigint NOT NULL,
  `enlace_original` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `enlace_acortado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_enlaces_solicitudes_solicitud` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_enlaces_solicitudes_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enlaces_subtipos`
--

DROP TABLE IF EXISTS `enlaces_subtipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enlaces_subtipos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_sub_tipo_solicitud` bigint NOT NULL,
  `nombre_requisito` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_enlaces_subtipos_id_sub_tipo_soli` (`id_sub_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_enlaces_subtipos_id_sub_tipo_soli` FOREIGN KEY (`id_sub_tipo_solicitud`) REFERENCES `sub_tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enlaces_tipos_solicitudes`
--

DROP TABLE IF EXISTS `enlaces_tipos_solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enlaces_tipos_solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_tipo_solicitud` bigint NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `url_real` varchar(400) NOT NULL,
  `url_acortada` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_enlaces_tipos_solicitudes_id_tipo_soli` (`id_tipo_solicitud`),
  CONSTRAINT `fk_enlaces_tipos_solicitudes_id_tipo_soli` FOREIGN KEY (`id_tipo_solicitud`) REFERENCES `tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudiantes`
--

DROP TABLE IF EXISTS `estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiantes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `discapacidad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `etnia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_poblacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ciudad_residencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `codigo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `correo_universidad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_grado` date DEFAULT NULL,
  `cohorte` int DEFAULT NULL,
  `es_estudiante_doctorado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado_maestria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `modalidad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_grupo_investigacion` bigint DEFAULT NULL,
  `modalidad_ingreso` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `periodo_ingreso` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `semestre_academico` int DEFAULT NULL,
  `semestre_financiero` int DEFAULT NULL,
  `titulo_doctorado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `observacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `titulo_pregrado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_persona` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_persona_estudiante` (`id_persona`) USING BTREE,
  KEY `idx_estudiantes_grupo_investigacion` (`id_grupo_investigacion`),
  CONSTRAINT `fk_estudiantes_grupo_investigacion` FOREIGN KEY (`id_grupo_investigacion`) REFERENCES `grupos_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_persona_estudiante` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `estudiantes_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `estudiantes_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudiantes_distinciones_academicas`
--

DROP TABLE IF EXISTS `estudiantes_distinciones_academicas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiantes_distinciones_academicas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_estudiante` bigint NOT NULL,
  `id_distincion_academica` bigint NOT NULL,
  `numero_resolucion` varchar(100) NOT NULL,
  `fecha_resolucion` date NOT NULL,
  `resolucion_pdf` mediumtext CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_estudiante_distincion_academica` (`id_estudiante`,`id_distincion_academica`),
  KEY `fk_estudiante_distincion_distincion` (`id_distincion_academica`),
  CONSTRAINT `fk_estudiante_distincion_distincion` FOREIGN KEY (`id_distincion_academica`) REFERENCES `distinciones_academicas` (`id`),
  CONSTRAINT `fk_estudiante_distincion_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudiantes_publicacion`
--

DROP TABLE IF EXISTS `estudiantes_publicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiantes_publicacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idpublicacion` int NOT NULL,
  `id_estudiante` int NOT NULL,
  PRIMARY KEY (`id`,`idpublicacion`,`id_estudiante`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluacion_curso_docente`
--

DROP TABLE IF EXISTS `evaluacion_curso_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion_curso_docente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_curso_docente` bigint NOT NULL,
  `id_evaluacion` bigint NOT NULL,
  `id_asignatura` bigint NOT NULL,
  `estado` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVO',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `evaluacion_curso_docente_asignaturas_FK` (`id_asignatura`) USING BTREE,
  KEY `evaluacion_curso_docente_curso_docente_FK` (`id_curso_docente`) USING BTREE,
  KEY `evaluacion_curso_docente_evaluacion_docente_FK` (`id_evaluacion`) USING BTREE,
  CONSTRAINT `evaluacion_curso_docente_asignaturas_FK` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evaluacion_curso_docente_curso_docente_FK` FOREIGN KEY (`id_curso_docente`) REFERENCES `curso_docente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evaluacion_curso_docente_evaluacion_docente_FK` FOREIGN KEY (`id_evaluacion`) REFERENCES `evaluacion_docente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluacion_docente`
--

DROP TABLE IF EXISTS `evaluacion_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion_docente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `periodo` int NOT NULL,
  `anio` int NOT NULL,
  `estado` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVO',
  `id_cuestionario` bigint NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_inicio` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_fin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `evaluacion_docente_cuestionarios_FK` (`id_cuestionario`) USING BTREE,
  CONSTRAINT `evaluacion_docente_cuestionarios_FK` FOREIGN KEY (`id_cuestionario`) REFERENCES `cuestionarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluacion_observacion`
--

DROP TABLE IF EXISTS `evaluacion_observacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion_observacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_evaluacion_curso_docente` bigint NOT NULL,
  `id_estudiante` bigint NOT NULL,
  `observacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `evaluacion_observacion_evaluacion_curso_docente_FK` (`id_evaluacion_curso_docente`) USING BTREE,
  KEY `evaluacion_observacion_estudiantes_FK` (`id_estudiante`) USING BTREE,
  CONSTRAINT `evaluacion_observacion_estudiantes_FK` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evaluacion_observacion_evaluacion_curso_docente_FK` FOREIGN KEY (`id_evaluacion_curso_docente`) REFERENCES `evaluacion_curso_docente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluacion_respuesta_estudiante`
--

DROP TABLE IF EXISTS `evaluacion_respuesta_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion_respuesta_estudiante` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_evaluacion_curso_docente` bigint NOT NULL,
  `id_estudiante` bigint NOT NULL,
  `id_pregunta` bigint NOT NULL,
  `valor_respuesta` int NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `evaluacion_respuesta_estudiante_evaluacion_curso_docente_FK` (`id_evaluacion_curso_docente`) USING BTREE,
  KEY `evaluacion_respuesta_estudiante_estudiantes_FK` (`id_estudiante`) USING BTREE,
  KEY `evaluacion_respuesta_estudiante_pregunta_FK` (`id_pregunta`) USING BTREE,
  CONSTRAINT `evaluacion_respuesta_estudiante_estudiantes_FK` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evaluacion_respuesta_estudiante_evaluacion_curso_docente_FK` FOREIGN KEY (`id_evaluacion_curso_docente`) REFERENCES `evaluacion_curso_docente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evaluacion_respuesta_estudiante_pregunta_FK` FOREIGN KEY (`id_pregunta`) REFERENCES `pregunta` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluaciones_docentes`
--

DROP TABLE IF EXISTS `evaluaciones_docentes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluaciones_docentes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_docente` bigint NOT NULL,
  `codigoeval` int NOT NULL,
  `periodoeval` int NOT NULL,
  `anioeval` int NOT NULL,
  `estadoeval` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_evaluacion_docente` (`id_docente`) USING BTREE,
  CONSTRAINT `fk_evaluacion_docente` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluadores`
--

DROP TABLE IF EXISTS `evaluadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluadores` (
  `id` int NOT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `evaluadores_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `evaluadores_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `examenes_valoracion_cancelado`
--

DROP TABLE IF EXISTS `examenes_valoracion_cancelado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `examenes_valoracion_cancelado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_trabajo_grado` bigint NOT NULL,
  `observacion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_trabajo_grado_cancelado` (`id_trabajo_grado`) USING BTREE,
  CONSTRAINT `fk_trabajo_grado_cancelado` FOREIGN KEY (`id_trabajo_grado`) REFERENCES `trabajos_grado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expertos`
--

DROP TABLE IF EXISTS `expertos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expertos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_persona` bigint NOT NULL,
  `tituloexper` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `universidadtitexp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `iddocidentidad` bigint DEFAULT NULL,
  `copiadocidentidad` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `universidadexp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `facultadexp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `grupoinvexp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `lineainvexp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `observacionexp` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_persona_experto` (`id_persona`) USING BTREE,
  CONSTRAINT `fk_persona_experto` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `expertos_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `expertos_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expertos_linea_investigacion`
--

DROP TABLE IF EXISTS `expertos_linea_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expertos_linea_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_experto` bigint DEFAULT NULL,
  `id_linea_investigacion` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_linea_investigacion` (`id_linea_investigacion`) USING BTREE,
  KEY `fk_experto` (`id_experto`) USING BTREE,
  CONSTRAINT `fk_experto` FOREIGN KEY (`id_experto`) REFERENCES `expertos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_linea_investigacion` FOREIGN KEY (`id_linea_investigacion`) REFERENCES `lineas_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `firmas_solicitud`
--

DROP TABLE IF EXISTS `firmas_solicitud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `firmas_solicitud` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `firma_tutor` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `firma_estudiante` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `firma_director` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `num_pagina_tutor` int DEFAULT NULL,
  `pos_x_tutor` decimal(10,4) DEFAULT NULL,
  `pos_y_tutor` decimal(10,4) DEFAULT NULL,
  `num_pagina_director` int DEFAULT NULL,
  `pos_x_director` decimal(10,4) DEFAULT NULL,
  `pos_y_director` decimal(10,4) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_firmas_sol_id_solicitud` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_firmas_sol_id_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `generaciones_resolucion`
--

DROP TABLE IF EXISTS `generaciones_resolucion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `generaciones_resolucion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codirector` bigint NOT NULL,
  `director` bigint NOT NULL,
  `concepto_documentos_coordinador` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_acta_consejo_facultad` date DEFAULT NULL,
  `link_anteproyecto_final` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_solicitud_comite` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_solicitud_consejo_facultad` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_acta_consejo_facultad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_oficio_consejo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_trabajo_grado` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_trabajo_grado_generacion` (`id_trabajo_grado`) USING BTREE,
  CONSTRAINT `fk_trabajo_grado_generacion` FOREIGN KEY (`id_trabajo_grado`) REFERENCES `trabajos_grado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `generar_reporte`
--

DROP TABLE IF EXISTS `generar_reporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `generar_reporte` (
  `id_reporte` int NOT NULL AUTO_INCREMENT,
  `id_coordinador` bigint NOT NULL,
  `tiporeporte` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `copiaarep` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nombreareamov` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tituloareamov` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `organizacionareamov` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `rolareamov` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nombrerelinter` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `titulorelinter` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `organizacionrelinter` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `rolrelinter` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id_reporte`) USING BTREE,
  KEY `fk_coord_reporte` (`id_coordinador`) USING BTREE,
  CONSTRAINT `fk_coord_reporte` FOREIGN KEY (`id_coordinador`) REFERENCES `coordinadores` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupos_investigacion`
--

DROP TABLE IF EXISTS `grupos_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grupos_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `sigla` varchar(20) NOT NULL,
  `estado` varchar(20) NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_grupos_investigacion_nombre` (`nombre`),
  UNIQUE KEY `uk_grupos_investigacion_sigla` (`sigla`),
  CONSTRAINT `chk_grupos_investigacion_estado` CHECK ((`estado` in (_utf8mb4'ACTIVO',_utf8mb4'INACTIVO')))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historial_estado_solicitudes`
--

DROP TABLE IF EXISTS `historial_estado_solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_estado_solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `pdf_base64` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `descripcion` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `comentarios` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_creacion` int DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_historial_estado_solicitud` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_historial_estado_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `homologaciones`
--

DROP TABLE IF EXISTS `homologaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `homologaciones` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_homologacion` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_homologacion` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `homologaciones_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `homologaciones_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lineas_investigacion`
--

DROP TABLE IF EXISTS `lineas_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lineas_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_categoria` bigint NOT NULL,
  `titulo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `descripcion` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVO',
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `lineas_investigacion_categorias_lineas_investigacion_FK` (`id_categoria`) USING BTREE,
  CONSTRAINT `lineas_investigacion_categorias_lineas_investigacion_FK` FOREIGN KEY (`id_categoria`) REFERENCES `categorias_lineas_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `lineas_investigacion_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `lineas_investigacion_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `materiales_apoyo`
--

DROP TABLE IF EXISTS `materiales_apoyo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materiales_apoyo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombrematerial` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `descripcionmaterial` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `enlacesmaterial` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `materiales_apoyo_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `materiales_apoyo_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `matricula_calificaciones`
--

DROP TABLE IF EXISTS `matricula_calificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matricula_calificaciones` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_matricula` int NOT NULL,
  `nota` decimal(3,2) NOT NULL,
  `es_definitiva` tinyint(1) DEFAULT '0',
  `fecha_registro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_asignatura` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `matricula_calificaciones_asignaturas_FK` (`id_asignatura`) USING BTREE,
  KEY `matricula_calificaciones_matriculas_FK` (`id_matricula`) USING BTREE,
  CONSTRAINT `matricula_calificaciones_asignaturas_FK` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `matricula_calificaciones_matriculas_FK` FOREIGN KEY (`id_matricula`) REFERENCES `matriculas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `matriculas`
--

DROP TABLE IF EXISTS `matriculas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matriculas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` bigint NOT NULL,
  `periodo` int DEFAULT NULL,
  `anio` int DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '1',
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_curso` bigint NOT NULL,
  `id_periodo` bigint DEFAULT NULL,
  `estado_matricula` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `observacion` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_matricula` (`id_estudiante`) USING BTREE,
  KEY `matriculas_cursos_FK` (`id_curso`) USING BTREE,
  KEY `matriculas_periodo_academico_FK` (`id_periodo`) USING BTREE,
  CONSTRAINT `fk_estudiante_matricula` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `matriculas_cursos_FK` FOREIGN KEY (`id_curso`) REFERENCES `cursos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `matriculas_periodo_academico_FK` FOREIGN KEY (`id_periodo`) REFERENCES `periodo_academico` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `matriculas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `matriculas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=1423 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movilidades`
--

DROP TABLE IF EXISTS `movilidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movilidades` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idexperto` bigint NOT NULL,
  `id_docente` bigint NOT NULL,
  `id_estudiante` bigint NOT NULL,
  `sentidomov` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `orimov` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `permisomov` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipomov` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fechaentradamov` date NOT NULL,
  `fechasalidamov` date NOT NULL,
  `diasestanciamov` int DEFAULT NULL,
  `aniomov` int NOT NULL,
  `universidadorig` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `existeconvenio` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `numeroconvenio` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tipoevento` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `descripcionevento` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `programarealizavisita` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `facultad` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `paisorigen` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ciudadorigen` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `direccionhospedaje` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tutormov` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `financiado` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `valorfinanciacion` int DEFAULT NULL,
  `fuentefinanciacion` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numeroactamov` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fechaactamov` date DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_docente_movilidad` (`id_docente`) USING BTREE,
  KEY `fk_estudiante_movilidad` (`id_estudiante`) USING BTREE,
  KEY `fk_mov_exp` (`idexperto`) USING BTREE,
  CONSTRAINT `fk_docente_movilidad` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_estudiante_movilidad` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_mov_exp` FOREIGN KEY (`idexperto`) REFERENCES `expertos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `movilidades_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `movilidades_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notas_documentos_requerido`
--

DROP TABLE IF EXISTS `notas_documentos_requerido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notas_documentos_requerido` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nota` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_req_solicitud` bigint NOT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_req_solicitud` (`id_req_solicitud`) USING BTREE,
  CONSTRAINT `fk_req_solicitud` FOREIGN KEY (`id_req_solicitud`) REFERENCES `requisitos_solicitud` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `notas_documentos_requerido_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `notas_documentos_requerido_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oficios`
--

DROP TABLE IF EXISTS `oficios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oficios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `asuntoofi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fechaoficio` date DEFAULT NULL,
  `numoficio` bigint DEFAULT NULL,
  `id_doc_maestria` bigint DEFAULT NULL,
  `estado` bit(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_documento_oficio` (`id_doc_maestria`) USING BTREE,
  CONSTRAINT `fk_documento_oficio` FOREIGN KEY (`id_doc_maestria`) REFERENCES `documentos_maestria` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `oficios_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `oficios_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `otros_documentos`
--

DROP TABLE IF EXISTS `otros_documentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otros_documentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion_documento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nombredocumento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `versiondoc` bigint DEFAULT NULL,
  `id_doc_maestria` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_documento_otros` (`id_doc_maestria`) USING BTREE,
  CONSTRAINT `fk_documento_otros` FOREIGN KEY (`id_doc_maestria`) REFERENCES `documentos_maestria` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `otros_documentos_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `otros_documentos_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pasantias`
--

DROP TABLE IF EXISTS `pasantias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pasantias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_estudiante` bigint NOT NULL,
  `creditospas` int NOT NULL,
  `numactapas` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fechaactapas` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `informepasantia` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `lugar_pasantia` varchar(255) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_pasantia2` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante_pasantia2` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `pasantias_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `pasantias_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `periodo_academico`
--

DROP TABLE IF EXISTS `periodo_academico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `periodo_academico` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `fecha_fin_matricula` date NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tag_periodo` int NOT NULL,
  `estado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `periodos_academicos`
--

DROP TABLE IF EXISTS `periodos_academicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `periodos_academicos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_fin_matricula` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `tag_periodo` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permisos`
--

DROP TABLE IF EXISTS `permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permisos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre_p` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ambito_p` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ruta_p` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `modulo_p` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `descripcion_p` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `accion_p` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permisos_roles`
--

DROP TABLE IF EXISTS `permisos_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permisos_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_rol` bigint NOT NULL,
  `id_permiso` bigint NOT NULL,
  PRIMARY KEY (`id`,`id_rol`,`id_permiso`) USING BTREE,
  KEY `fk_permiso_rol` (`id_rol`) USING BTREE,
  KEY `fk_permiso_rol2` (`id_permiso`) USING BTREE,
  CONSTRAINT `fk_permiso_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_permiso_rol2` FOREIGN KEY (`id_permiso`) REFERENCES `permisos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personas`
--

DROP TABLE IF EXISTS `personas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `correo_electronico` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `genero` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `identificacion` bigint DEFAULT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `telefono` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_identificacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `personas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `personas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `practicas`
--

DROP TABLE IF EXISTS `practicas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `practicas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_estudiante` bigint NOT NULL,
  `codigoprac` int NOT NULL,
  `creditosprac` int NOT NULL,
  `numactaprac` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fechaactaprac` date DEFAULT NULL,
  `solicitudcreditos` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `horastotales` int DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_est_prac` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_est_prac` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `practicas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `practicas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pregunta`
--

DROP TABLE IF EXISTS `pregunta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pregunta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `observacion` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVO',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `pregunta_unique` (`nombre`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `preguntas`
--

DROP TABLE IF EXISTS `preguntas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preguntas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `numpregunta` int NOT NULL,
  `nombrepregunta` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `observacionpreg` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fechacreacion` date DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `preguntas_cuestionarios`
--

DROP TABLE IF EXISTS `preguntas_cuestionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preguntas_cuestionarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_pregunta` bigint NOT NULL,
  `id_cuestionario` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UKc6l23a93m89qudxg37mahsw2j` (`id_cuestionario`,`id_pregunta`) USING BTREE,
  KEY `pregunta_cuestionario_pregunta_FK` (`id_pregunta`) USING BTREE,
  CONSTRAINT `pregunta_cuestionario_pregunta_FK` FOREIGN KEY (`id_pregunta`) REFERENCES `pregunta` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `preguntas_cuestionarios_cuestionarios_FK` FOREIGN KEY (`id_cuestionario`) REFERENCES `cuestionarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prorrogas`
--

DROP TABLE IF EXISTS `prorrogas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prorrogas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `link_documento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `resolucion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `soporte` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo_prorroga` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_estudiante` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_est_pro` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_est_pro` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `prorrogas_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `prorrogas_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `publicaciones`
--

DROP TABLE IF EXISTS `publicaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publicaciones` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigopubli` int NOT NULL,
  `titulopubli` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tipopub` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `creditospub` int DEFAULT NULL,
  `numactapub` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fechaactapub` date DEFAULT NULL,
  `indexadapub` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fechaaceptacion` date DEFAULT NULL,
  `linkcartaaceptacion` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombrerevista` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `isbnpub` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `linkpublicacion` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `otrosautores` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `publicaciones_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `publicaciones_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reconocimiento_creditos`
--

DROP TABLE IF EXISTS `reconocimiento_creditos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reconocimiento_creditos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `tipo_reconocimiento` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_rec_creditos` (`id_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud_rec_creditos` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reingresos`
--

DROP TABLE IF EXISTS `reingresos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reingresos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `link_documento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `resolucion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `semestre_academico` int DEFAULT NULL,
  `semestre_financiero` int DEFAULT NULL,
  `id_estudiante` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante_reingreso` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante_reingreso` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reingresos_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `reingresos_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reportes_expertos`
--

DROP TABLE IF EXISTS `reportes_expertos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reportes_expertos` (
  `id_reporte_externo` bigint NOT NULL AUTO_INCREMENT,
  `id_coordinador` bigint NOT NULL,
  `copiarep` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nombreremitentemov` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tituloprofesionalmov` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `organizacionmov` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `rolorganizacionmov` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nombredirigerep` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `titulodirigerep` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `organizacionrep` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `roloranizacionrep` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id_reporte_externo`) USING BTREE,
  KEY `fk_coordinador_reporte_experto` (`id_coordinador`) USING BTREE,
  CONSTRAINT `fk_coordinador_reporte_experto` FOREIGN KEY (`id_coordinador`) REFERENCES `coordinadores` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `requisitos_solicitud`
--

DROP TABLE IF EXISTS `requisitos_solicitud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requisitos_solicitud` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo_documento` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `articulo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `tener_en_cuenta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `id_tipo_solicitud` bigint NOT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud` (`id_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_solicitud` FOREIGN KEY (`id_tipo_solicitud`) REFERENCES `tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `requisitos_solicitud_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `requisitos_solicitud_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `respuestas_comite_generacion_resolucion`
--

DROP TABLE IF EXISTS `respuestas_comite_generacion_resolucion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `respuestas_comite_generacion_resolucion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `concepto_comite` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha_acta` date NOT NULL,
  `numero_acta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_generacion_resolucion` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_generacion_resolucion` (`id_generacion_resolucion`) USING BTREE,
  CONSTRAINT `fk_generacion_resolucion` FOREIGN KEY (`id_generacion_resolucion`) REFERENCES `generaciones_resolucion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `respuestas_comite_solicitud_examen_valoracion`
--

DROP TABLE IF EXISTS `respuestas_comite_solicitud_examen_valoracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `respuestas_comite_solicitud_examen_valoracion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `concepto_comite` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha_acta` date NOT NULL,
  `numero_acta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_examen_valoracion` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitud_examen_valoracion_respuesta_comite` (`id_examen_valoracion`) USING BTREE,
  CONSTRAINT `fk_solicitud_examen_valoracion_respuesta_comite` FOREIGN KEY (`id_examen_valoracion`) REFERENCES `solicitudes_examen_valoracion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `respuestas_comite_sustentacion`
--

DROP TABLE IF EXISTS `respuestas_comite_sustentacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `respuestas_comite_sustentacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `concepto_comite` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha_acta` date NOT NULL,
  `numero_acta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_sustentacion_proyecto_investigacion` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_sustentacion` (`id_sustentacion_proyecto_investigacion`) USING BTREE,
  CONSTRAINT `fk_sustentacion` FOREIGN KEY (`id_sustentacion_proyecto_investigacion`) REFERENCES `sustentaciones_proyecto_investigacion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `respuestas_examen_valoracion`
--

DROP TABLE IF EXISTS `respuestas_examen_valoracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `respuestas_examen_valoracion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_maxima_entrega` date DEFAULT NULL,
  `id_evaluador` bigint NOT NULL,
  `link_formatob` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_formatoc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_observaciones` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `respuesta_examen_valoracion` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tipo_evaluador` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_trabajo_grado` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_trabajo_grado_respuesta` (`id_trabajo_grado`) USING BTREE,
  CONSTRAINT `fk_trabajo_grado_respuesta` FOREIGN KEY (`id_trabajo_grado`) REFERENCES `trabajos_grado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles_informacion`
--

DROP TABLE IF EXISTS `roles_informacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_informacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_rol` bigint NOT NULL,
  `nombre_completo` varchar(255) NOT NULL,
  `titulo` varchar(100) DEFAULT NULL,
  `cargo` varchar(100) NOT NULL,
  `tratamiento` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_roles_informacion_rol` (`id_rol`),
  CONSTRAINT `fk_roles_informacion_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solicitud_beca_descuento`
--

DROP TABLE IF EXISTS `solicitud_beca_descuento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitud_beca_descuento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `tipo` varchar(200) NOT NULL,
  `motivo` text,
  `formato_solicitud` mediumtext,
  PRIMARY KEY (`id`),
  KEY `fk_solicitud_beca_descuento_id_solicitud` (`id_solicitud`),
  CONSTRAINT `fk_solicitud_beca_descuento_id_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solicitudes`
--

DROP TABLE IF EXISTS `solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_tipo_solicitud` bigint NOT NULL,
  `id_estudiante` bigint NOT NULL,
  `id_tutor` bigint NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `requiere_firma_director` tinyint(1) NOT NULL DEFAULT '0',
  `documento_firmado` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `radicado` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_director` bigint DEFAULT NULL,
  `id_revisor` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_solicitudes_id_tipo_soli` (`id_tipo_solicitud`) USING BTREE,
  KEY `fk_solicitudes_id_estudiante` (`id_estudiante`) USING BTREE,
  KEY `fk_solicitudes_id_tutor` (`id_tutor`) USING BTREE,
  KEY `fk_solicitudes_id_director` (`id_director`),
  CONSTRAINT `fk_solicitudes_id_director` FOREIGN KEY (`id_director`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solicitudes_id_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solicitudes_id_tipo_soli` FOREIGN KEY (`id_tipo_solicitud`) REFERENCES `tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solicitudes_id_tutor` FOREIGN KEY (`id_tutor`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `solicitudes_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `solicitudes_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solicitudes_en_comite`
--

DROP TABLE IF EXISTS `solicitudes_en_comite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitudes_en_comite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `avalado_comite` varchar(2) DEFAULT NULL,
  `concepto_comite` text,
  `numero_acta` varchar(200) DEFAULT NULL,
  `fecha_aval` date DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_solicitud_comite_id_sol` (`id_solicitud`),
  CONSTRAINT `fk_solicitud_comite_id_sol` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solicitudes_en_concejo`
--

DROP TABLE IF EXISTS `solicitudes_en_concejo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitudes_en_concejo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_solicitud` bigint NOT NULL,
  `avalado_concejo` varchar(2) DEFAULT NULL,
  `concepto_concejo` text,
  `numero_acta` varchar(200) DEFAULT NULL,
  `fecha_aval` date DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_solicitud_concejo_id_sol` (`id_solicitud`),
  CONSTRAINT `fk_solicitud_concejo_id_sol` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solicitudes_examen_valoracion`
--

DROP TABLE IF EXISTS `solicitudes_examen_valoracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitudes_examen_valoracion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `concepto_coordinador_documentos` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_maxima_evaluacion` date DEFAULT NULL,
  `id_evaluador_externo` bigint NOT NULL,
  `id_evaluador_interno` bigint NOT NULL,
  `link_formatoa` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_formatod` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_formatoe` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_oficio_dirigido_evaluadores` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `titulo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_trabajo_grado` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_trabajo_grado_solicitud` (`id_trabajo_grado`) USING BTREE,
  CONSTRAINT `fk_trabajo_grado_solicitud` FOREIGN KEY (`id_trabajo_grado`) REFERENCES `trabajos_grado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sub_tipos_solicitudes`
--

DROP TABLE IF EXISTS `sub_tipos_solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sub_tipos_solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_tipo_solicitud` bigint NOT NULL,
  `codigo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `peso` double DEFAULT NULL,
  `horas_asignadas` int DEFAULT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_sub_tipo_solicitud_id_tipo_soli` (`id_tipo_solicitud`) USING BTREE,
  CONSTRAINT `fk_sub_tipo_solicitud_id_tipo_soli` FOREIGN KEY (`id_tipo_solicitud`) REFERENCES `tipos_solicitudes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sub_tipos_solicitudes_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `sub_tipos_solicitudes_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sustentaciones_proyecto_investigacion`
--

DROP TABLE IF EXISTS `sustentaciones_proyecto_investigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sustentaciones_proyecto_investigacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `link_monografia` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `concepto_coordinador` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_acta_consejo` date DEFAULT NULL,
  `fecha_acta_final` date DEFAULT NULL,
  `id_jurado_externo` bigint NOT NULL,
  `id_jurado_interno` bigint NOT NULL,
  `jurados_aceptados` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fecha_sustentacion` date DEFAULT NULL,
  `link_acta_sustentacion_publica` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_estudio_hoja_vida_academica` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_estudio_hoja_vida_academica_grado` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_formatof` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_formatog` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_formatoh` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_formatoi` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_acta_consejo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_acta_final` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_oficio_consejo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `respuesta_sustentacion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_trabajo_grado` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_trabajo_grado_sustentacion` (`id_trabajo_grado`) USING BTREE,
  CONSTRAINT `fk_trabajo_grado_sustentacion` FOREIGN KEY (`id_trabajo_grado`) REFERENCES `trabajos_grado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tiempos_pendientes`
--

DROP TABLE IF EXISTS `tiempos_pendientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tiempos_pendientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` int NOT NULL,
  `fecha_limite` date NOT NULL,
  `fecha_registro` date NOT NULL,
  `id_trabajo_grado` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_trabajo_grado_tiempos` (`id_trabajo_grado`) USING BTREE,
  CONSTRAINT `fk_trabajo_grado_tiempos` FOREIGN KEY (`id_trabajo_grado`) REFERENCES `trabajos_grado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipos_solicitudes`
--

DROP TABLE IF EXISTS `tipos_solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipos_solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_inicio` varchar(10) DEFAULT NULL,
  `fecha_final` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `tipos_solicitudes_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `tipos_solicitudes_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `titulos`
--

DROP TABLE IF EXISTS `titulos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `titulos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `abreviatura` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `categoria_min_ciencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link_cv_lac` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `universidad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_docente` bigint DEFAULT NULL,
  `usuario_creacion` int NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modificacion` int NOT NULL DEFAULT '1',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_docente_titulo` (`id_docente`) USING BTREE,
  CONSTRAINT `fk_docente_titulo` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `titulos_chk_1` CHECK ((`usuario_creacion` > 0)),
  CONSTRAINT `titulos_chk_2` CHECK ((`usuario_modificacion` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trabajos_grado`
--

DROP TABLE IF EXISTS `trabajos_grado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trabajos_grado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `correo_electronico_tutor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha_creacion` date NOT NULL,
  `id_estudiante` bigint NOT NULL,
  `numero_estado` int NOT NULL,
  `titulo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_estudiante` (`id_estudiante`) USING BTREE,
  CONSTRAINT `fk_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contrasena` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios_roles`
--

DROP TABLE IF EXISTS `usuarios_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_rol` bigint NOT NULL,
  `id_usuario` bigint NOT NULL,
  PRIMARY KEY (`id`,`id_rol`,`id_usuario`) USING BTREE,
  KEY `fk_usuario_rol` (`id_rol`) USING BTREE,
  KEY `fk_usuario_rol2` (`id_usuario`) USING BTREE,
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_usuario_rol2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-14 16:47:45
