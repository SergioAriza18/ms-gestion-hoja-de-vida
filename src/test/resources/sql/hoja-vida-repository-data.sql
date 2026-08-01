INSERT INTO asignaturas (id, codigo_asignatura, nombre_asignatura, creditos, area_formacion) VALUES
    (1, 'M10001', 'Fundamentos de computación', 4, 5),
    (2, 'M10002', 'Electiva avanzada', 3, 6),
    (3, 'M27708', 'Seminario de investigación', 4, 7),
    (4, 'M10003', 'Competencias empresariales', 2, 8),
    (5, 'M27712', 'Trabajo de grado II', 4, 9),
    (6, 'M27709', 'Trabajo de grado I', 4, 7),
    (7, 'M99999', 'Asignatura no definitiva', 5, 5);

INSERT INTO matriculas (id, id_estudiante, anio, periodo) VALUES
    (1, 1, 2024, 1);

INSERT INTO matricula_calificaciones (id, id_matricula, id_asignatura, nota, es_definitiva, fecha_registro) VALUES
    (1, 1, 1, 4.0, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (2, 1, 2, 3.4, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (3, 1, 3, 5.0, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (4, 1, 4, 3.5, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (5, 1, 5, 5.0, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (6, 1, 6, 3.4, TRUE, TIMESTAMP '2024-06-01 10:00:00'),
    (7, 1, 7, 5.0, FALSE, TIMESTAMP '2024-06-01 10:00:00');

INSERT INTO publicaciones (id, creditospub, numactapub, titulopubli, tipopub, indexadapub, fechaaceptacion, linkpublicacion) VALUES
    (1, 1, 'ACT-PUB-1', 'Artículo de investigación', 'Revista', 'Q2', DATE '2024-04-10', 'https://example.test/publicacion'),
    (2, NULL, 'ACT-PUB-2', 'Publicación sin créditos', 'Revista', 'Q3', DATE '2024-04-11', 'https://example.test/publicacion-sin-creditos');

INSERT INTO estudiantes_publicacion (id_estudiante, idpublicacion) VALUES
    (1, 1),
    (1, 2);

INSERT INTO pasantias (id, id_estudiante, creditospas, numactapas, fechaactapas, informepasantia, estado) VALUES
    (1, 1, 2, 'ACT-PAS-1', '2024-05-20', 'Informe de pasantía', TRUE),
    (2, 1, -1, 'ACT-PAS-2', '2024-05-21', 'Créditos no válidos', TRUE);

INSERT INTO practicas (id, id_estudiante, creditosprac, numactaprac, horastotales) VALUES
    (1, 1, 1, 'ACT-PRA-1', 64),
    (2, 1, -1, 'ACT-PRA-2', 32);

INSERT INTO trabajos_grado (id, id_estudiante, titulo) VALUES
    (1, 1, 'Sistema académico'),
    (2, 2, 'Investigación aplicada');

INSERT INTO docentes (id, id_persona) VALUES
    (1, 10),
    (2, 11);

INSERT INTO generaciones_resolucion (id, id_trabajo_grado, director, codirector) VALUES
    (1, 1, 1, 2),
    (2, 2, 1, NULL);
