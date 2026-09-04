CREATE TABLE IF NOT EXISTS matriculas (
    id BIGINT PRIMARY KEY,
    id_estudiante BIGINT NOT NULL,
    anio INT,
    periodo INT
);

CREATE TABLE IF NOT EXISTS asignaturas (
    id BIGINT PRIMARY KEY,
    codigo_asignatura VARCHAR(50),
    nombre_asignatura VARCHAR(255),
    creditos INT,
    area_formacion BIGINT
);

CREATE TABLE IF NOT EXISTS trabajos_grado (
    id BIGINT PRIMARY KEY,
    id_estudiante BIGINT NOT NULL,
    titulo VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS docentes (
    id BIGINT PRIMARY KEY,
    id_persona BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS generaciones_resolucion (
    id BIGINT PRIMARY KEY,
    id_trabajo_grado BIGINT NOT NULL,
    director BIGINT,
    codirector BIGINT
);

CREATE TABLE IF NOT EXISTS estudiantes_publicacion (
    id_estudiante BIGINT NOT NULL,
    idpublicacion BIGINT NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_estudiante_distincion_academica
    ON estudiantes_distinciones_academicas (id_estudiante, id_distincion_academica);
