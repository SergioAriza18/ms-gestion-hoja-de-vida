    USE BD_distinciones;

    CREATE TABLE distinciones_academicas (
        id BIGINT NOT NULL AUTO_INCREMENT,
        codigo VARCHAR(50) NOT NULL,
        nombre VARCHAR(150) NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT uk_distincion_academica_codigo UNIQUE (codigo)
    );

    INSERT INTO distinciones_academicas (codigo, nombre) VALUES
        ('EXCELENCIA_ACADEMICA', 'Excelencia académica'),
        ('MENCION_HONOR_TRABAJO_GRADO', 'Mención de honor en trabajo de grado');

    CREATE TABLE estudiantes_distinciones_academicas (
        id BIGINT NOT NULL AUTO_INCREMENT,
        id_estudiante BIGINT NOT NULL,
        id_distincion_academica BIGINT NOT NULL,
        numero_resolucion VARCHAR(100) NOT NULL,
        fecha_resolucion DATE NOT NULL,
        resolucion_pdf LONGBLOB NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT uk_estudiante_distincion_academica
            UNIQUE (id_estudiante, id_distincion_academica),
        CONSTRAINT fk_estudiante_distincion_estudiante
            FOREIGN KEY (id_estudiante) REFERENCES estudiantes (id),
        CONSTRAINT fk_estudiante_distincion_distincion
            FOREIGN KEY (id_distincion_academica) REFERENCES distinciones_academicas (id)
    );
