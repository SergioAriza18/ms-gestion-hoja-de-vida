DELETE FROM estudiantes_distinciones_academicas;
DELETE FROM distinciones_academicas;
DELETE FROM estudiantes_publicacion;
DELETE FROM generaciones_resolucion;
DELETE FROM docentes;
DELETE FROM trabajos_grado;
DELETE FROM matricula_calificaciones;
DELETE FROM matriculas;
DELETE FROM asignaturas;
DELETE FROM pasantias;
DELETE FROM actividades;
DELETE FROM practicas;
DELETE FROM publicaciones;
DELETE FROM estudiantes;
DELETE FROM grupos_investigacion;
DELETE FROM personas;

INSERT INTO personas (id, nombre, apellido, identificacion, tipo_identificacion) VALUES
    (1, 'Laura', 'Gómez', 123456789, 'CC'),
    (2, 'Carlos', 'Pérez', 987654321, 'CC'),
    (3, 'Ana', 'López', 456789123, 'CC'),
    (10, 'Diana', 'Torres', 111111111, 'CC'),
    (11, 'Andrés', 'Ruiz', 222222222, 'CC');

INSERT INTO grupos_investigacion (id, nombre, sigla) VALUES
    (1, 'Grupo de I+D en Tecnologías de la Información', 'GTI'),
    (2, 'Grupo Investigación y Desarrollo en Ingeniería de Software', 'IDIS'),
    (3, 'Grupo de Investigación en Inteligencia Computacional', 'GICO');

INSERT INTO estudiantes
    (id, codigo, correo_universidad, semestre_academico, periodo_ingreso, estado_maestria, modalidad, id_grupo_investigacion, id_persona)
VALUES
    (1, '2024001', 'laura.gomez@universidad.edu', 2, '2024-1', 'ACTIVO', 'INVESTIGACION', 1, 1),
    (2, '2023002', 'carlos.perez@universidad.edu', 4, '2023-2', 'MAESTRIA_FINALIZADA', 'PROFUNDIZACION', 2, 2),
    (3, '2022003', 'ana.lopez@universidad.edu', 1, '2022-1', NULL, NULL, NULL, 3);

INSERT INTO distinciones_academicas (id, codigo, nombre) VALUES
    (1, 'EXCELENCIA_ACADEMICA', 'Excelencia académica'),
    (2, 'MENCION_HONOR_TRABAJO_GRADO', 'Mención de honor en trabajo de grado');
