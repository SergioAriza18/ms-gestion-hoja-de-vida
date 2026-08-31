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
DELETE FROM practicas;
DELETE FROM publicaciones;
DELETE FROM estudiantes;
DELETE FROM personas;

INSERT INTO personas (id, nombre, apellido, identificacion, tipo_identificacion) VALUES
    (1, 'Laura', 'Gómez', 123456789, 'CC'),
    (2, 'Carlos', 'Pérez', 987654321, 'CC'),
    (3, 'Ana', 'López', 456789123, 'CC'),
    (10, 'Diana', 'Torres', 111111111, 'CC'),
    (11, 'Andrés', 'Ruiz', 222222222, 'CC');

INSERT INTO estudiantes (id, codigo, correo_universidad, titulo_pregrado, fecha_grado, semestre_academico, periodo_ingreso, id_persona) VALUES
    (1, '2024001', 'laura.gomez@universidad.edu', 'Ingeniera de Sistemas', DATE '2023-12-15', 2, '2024-1', 1),
    (2, '2023002', 'carlos.perez@universidad.edu', 'Ingeniero Electrónico', DATE '2022-12-15', 4, '2023-2', 2),
    (3, '2022003', 'ana.lopez@universidad.edu', 'Ingeniera Industrial', DATE '2021-12-15', 1, '2022-1', 3);

INSERT INTO distinciones_academicas (id, codigo, nombre) VALUES
    (1, 'EXCELENCIA_ACADEMICA', 'Excelencia académica'),
    (2, 'MENCION_HONOR_TRABAJO_GRADO', 'Mención de honor en trabajo de grado');
