INSERT INTO asignaturas (id, codigo_asignatura, nombre_asignatura, creditos, area_formacion) VALUES
    (8, 'PSI POSG_MC', 'Prueba de Suficiencia Idioma Extranjero', 0, 9);

INSERT INTO matriculas (id, id_estudiante, anio, periodo) VALUES
    (2, 2, 2024, 1),
    (3, 3, 2024, 1);

INSERT INTO matricula_calificaciones
    (id, id_matricula, id_asignatura, nota, es_definitiva, fecha_registro) VALUES
    (8, 1, 8, 5.0, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (9, 2, 8, 3.0, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (10, 3, 8, 5.0, NULL, TIMESTAMP '2024-06-01 10:00:00');
