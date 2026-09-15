-- ============================================================================
-- Catalogos requeridos por Hoja de Vida y autenticacion
-- Generado a partir del volcado local del 14-09-2026.
-- Ejecutar despues de 01-estructura.sql y sobre una base de datos de prueba.
-- ============================================================================
USE `maestriacomputacion_HV`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET UNIQUE_CHECKS = 0;

-- Tabla: areas_formacion
INSERT INTO `areas_formacion` VALUES (1,'Asignaturas básicas y fundamentales del programa','Área de Fundamentación'),(2,'Asignaturas electivas y de profundización','Área de Electivas'),(3,'Asignaturas relacionadas con investigación y trabajo de grado','Área de Investigación'),(4,'Asignaturas de complementación e información de prácticas','Área de Complementación'),(5,'Requisitos obligatorios para graduarse','Requisitos de Grado');

-- Tabla: categorias_lineas_investigacion
-- Sin registros en el volcado de origen.

-- Tabla: lineas_investigacion
-- Sin registros en el volcado de origen.

-- Tabla: grupos_investigacion
INSERT INTO `grupos_investigacion` VALUES (1,'Grupo de I+D en Tecnologías de la Información','GTI','ACTIVO'),(2,'Grupo Investigación y Desarrollo en Ingeniería de Software','IDIS','ACTIVO'),(3,'Grupo de Investigación en Inteligencia Computacional','GICO','ACTIVO');

-- Tabla: distinciones_academicas
INSERT INTO `distinciones_academicas` VALUES (1,'EXCELENCIA_ACADEMICA','Excelencia académica'),(2,'MENCION_HONOR_TRABAJO_GRADO','Mención de honor en trabajo de grado');

-- Tabla: periodo_academico
INSERT INTO `periodo_academico` VALUES (9,'2025-06-01','2025-12-10','2025-12-01',NULL,2,'INACTIVO'),(10,'2026-01-01','2026-03-31','2026-03-01',NULL,1,'ACTIVO');

-- Tabla: asignaturas
INSERT INTO `asignaturas` VALUES (31,'M28955',NULL,3,b'1','2026-01-20 04:02:40.000000',72,48,120,'Fundamentos de diseño de software',NULL,'Obligatoria',1,NULL,NULL,NULL,NULL,1,'2026-01-20 09:02:40',1,'2026-01-20 09:02:40'),(32,'A-9',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Minería de Datos',NULL,'Obligatoria',1,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(33,'M32638',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Aprendizaje profundo',NULL,'Obligatoria',1,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(34,'M27701',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Gestión de la Tecnología y la innovación',NULL,'Obligatoria',1,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(35,'A-3',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Ingeniería de la usabilidad',NULL,'Obligatoria',1,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(36,'M27691',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Metodología de la investigación',NULL,'Obligatoria',1,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(37,'M27700',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Seminario de matemáticas',NULL,'Obligatoria',1,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(38,'M31187',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Aprendizaje de máquina en bioinformática',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(39,'A-2',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Calidad de software',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(40,'M27714',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Ingeniería de la Colaboración',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(41,'A-13',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Ingeniería de Procesos de Software',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(42,'M30915',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Metodologías ágiles para la gestión de proyectos en el desarrollo de software',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(43,'M24580',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Optimización usando metaheurísticas',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(44,'A-10',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Web Semántica',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(45,'M32636',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Electiva Internet de las cosas',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(46,'A-12',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Tópicos avanzados en bodegas de datos',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(47,'MC_A2',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Calidad de Producto de Software',NULL,'Electiva',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(48,'OB-11',NULL,1,b'1','2026-01-20 04:06:47.000000',24,16,40,'Competencias empresariales',NULL,'Opcional',4,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(49,'M33864',NULL,3,b'1','2026-01-20 04:06:47.000000',72,48,120,'Tópicos especiales en ciencia de datos: publicación y visualización',NULL,'Obligatoria',2,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(50,'M27706',NULL,6,b'1','2026-01-20 04:06:47.000000',144,96,240,'Propuesta de trabajo de grado',NULL,'Obligatoria',3,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(51,'M27708',NULL,2,b'1','2026-01-20 04:06:47.000000',48,32,80,'Seminario de investigación',NULL,'Obligatoria',3,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(52,'M27709',NULL,8,b'1','2026-01-20 04:06:47.000000',192,128,320,'Trabajo de grado 1',NULL,'Obligatoria',3,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(53,'M27712',NULL,8,b'1','2026-01-20 04:06:47.000000',192,128,320,'Trabajo de grado 2',NULL,'Obligatoria',3,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47'),(54,'PSI POSG_MC',NULL,0,b'1','2026-01-20 04:06:47.000000',30,50,80,'Prueba de Suficiencia Idioma Extranjero',NULL,'Obligatoria',5,NULL,NULL,NULL,NULL,1,'2026-01-20 09:06:47',1,'2026-01-20 09:06:47');

-- Tabla: tipos_solicitudes requeridos por la integración con Hoja de Vida
INSERT INTO `tipos_solicitudes`
    (`id`, `codigo`, `nombre`, `estado`, `usuario_creacion`, `fecha_creacion`,
     `usuario_modificacion`, `fecha_modificacion`, `fecha_inicio`, `fecha_final`)
VALUES
    (9001, 'HO_ASIG_POS', 'Homologación de asignaturas', 'ACTIVO', 1,
     '2026-09-14 00:00:00', 1, '2026-09-14 00:00:00', NULL, NULL),
    (9002, 'CA_ASIG', 'Cancelación de asignaturas', 'ACTIVO', 1,
     '2026-09-14 00:00:00', 1, '2026-09-14 00:00:00', NULL, NULL);

-- Tabla: roles
-- Sin registros en el volcado de origen.

-- Tabla: permisos
-- Sin registros en el volcado de origen.

-- Tabla: permisos_roles
-- Sin registros en el volcado de origen.

-- Tabla: roles_informacion
-- Sin registros en el volcado de origen.

SET UNIQUE_CHECKS = 1;
SET FOREIGN_KEY_CHECKS = 1;
