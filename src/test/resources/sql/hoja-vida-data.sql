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
    (10, 'Diana', 'Torres', 111111111, 'CC'),
    (11, 'Andrés', 'Ruiz', 222222222, 'CC');

INSERT INTO estudiantes (id, codigo, correo_universidad, titulo_pregrado, fecha_grado, semestre_academico, periodo_ingreso, id_persona) VALUES
    (1, '2024001', 'laura.gomez@universidad.edu', 'Ingeniera de Sistemas', DATE '2023-12-15', 2, '2024-1', 1),
    (2, '2023002', 'carlos.perez@universidad.edu', 'Ingeniero Electrónico', DATE '2022-12-15', 4, '2023-2', 2);

INSERT INTO asignaturas (id, codigo_asignatura, nombre_asignatura, creditos, area_formacion) VALUES
    (1, 'M10001', 'Fundamentos de computación', 4, 5),
    (2, 'M10002', 'Electiva avanzada', 3, 6),
    (3, 'M27708', 'Seminario de investigación', 4, 7),
    (4, 'M10003', 'Competencias empresariales', 2, 8),
    (5, 'M27712', 'Trabajo de grado II', 4, 9);

INSERT INTO matriculas (id, id_estudiante, anio, periodo) VALUES
    (1, 1, 2024, 1);

INSERT INTO matricula_calificaciones (id, id_matricula, id_asignatura, nota, es_definitiva, fecha_registro) VALUES
    (1, 1, 1, 4.0, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (2, 1, 2, 3.4, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (3, 1, 3, 5.0, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (4, 1, 4, 3.5, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (5, 1, 5, 5.0, TRUE, TIMESTAMP '2024-06-01 10:00:00');

INSERT INTO pasantias (id, id_estudiante, creditospas, numactapas, fechaactapas, informepasantia, estado) VALUES
    (1, 1, 2, 'ACT-PAS-1', '2024-05-20', 'Informe de pasantía', TRUE);

INSERT INTO publicaciones (id, creditospub, numactapub, titulopubli, tipopub, indexadapub, fechaaceptacion, linkpublicacion) VALUES
    (1, 1, 'ACT-PUB-1', 'Artículo de investigación', 'Revista', 'Q2', DATE '2024-04-10', 'https://example.test/publicacion');

INSERT INTO estudiantes_publicacion (id_estudiante, idpublicacion) VALUES
    (1, 1);

INSERT INTO practicas (id, id_estudiante, creditosprac, numactaprac, horastotales) VALUES
    (1, 1, 1, 'ACT-PRA-1', 64);

INSERT INTO trabajos_grado (id, id_estudiante, titulo) VALUES
    (1, 1, 'Sistema académico');

INSERT INTO docentes (id, id_persona) VALUES
    (1, 10),
    (2, 11);

INSERT INTO generaciones_resolucion (id, id_trabajo_grado, director, codirector) VALUES
    (1, 1, 1, 2);
