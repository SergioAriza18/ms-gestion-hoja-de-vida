package com.maestria.gestion.hoja_de_vida.repository;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCodigo(String codigo);

    Optional<Estudiante> findByPersonaIdentificacion(Long identificacion);

    List<Estudiante> findAllByPersonaNombreStartingWithIgnoreCase(String nombre, Sort sort);

    List<Estudiante> findAllBySemestreAcademico(Integer semestreAcademico, Sort sort);

    @Query(value = """
            SELECT estudiante.*
            FROM estudiantes estudiante
            WHERE (:semestreActual IS NULL OR estudiante.semestre_academico = :semestreActual)
              AND CASE WHEN EXISTS (
                      SELECT 1
                      FROM matricula_calificaciones calificacion
                      INNER JOIN matriculas matricula
                          ON matricula.id = calificacion.id_matricula
                      INNER JOIN asignaturas asignatura
                          ON asignatura.id = calificacion.id_asignatura
                      WHERE matricula.id_estudiante = estudiante.id
                        AND UPPER(TRIM(asignatura.codigo_asignatura)) = UPPER(:codigoSuficienciaIdioma)
                        AND calificacion.es_definitiva = 1
                        AND calificacion.nota >= :notaAprobatoria
                  ) THEN TRUE ELSE FALSE END = :suficienciaIdiomaAprobada
            ORDER BY estudiante.periodo_ingreso DESC
            """, nativeQuery = true)
    List<Estudiante> findAllBySuficienciaIdioma(
            @Param("suficienciaIdiomaAprobada") Boolean suficienciaIdiomaAprobada,
            @Param("semestreActual") Integer semestreActual,
            @Param("codigoSuficienciaIdioma") String codigoSuficienciaIdioma,
            @Param("notaAprobatoria") BigDecimal notaAprobatoria);

    @Query(value = """
            SELECT trabajo_grado.titulo
            FROM trabajos_grado trabajo_grado
            WHERE trabajo_grado.id_estudiante = :idEstudiante
            """, nativeQuery = true)
    Optional<String> findTituloTesisByEstudianteId(@Param("idEstudiante") Long idEstudiante);

    @Query(value = """
            SELECT
                NULLIF(TRIM(CONCAT(COALESCE(persona_director.nombre, ''), ' ', COALESCE(persona_director.apellido, ''))), '') AS director,
                NULLIF(TRIM(CONCAT(COALESCE(persona_codirector.nombre, ''), ' ', COALESCE(persona_codirector.apellido, ''))), '') AS codirector
            FROM trabajos_grado tg
            LEFT JOIN generaciones_resolucion gr
                ON gr.id_trabajo_grado = tg.id
            LEFT JOIN docentes docente_director
                ON docente_director.id = gr.director
            LEFT JOIN personas persona_director
                ON persona_director.id = docente_director.id_persona
            LEFT JOIN docentes docente_codirector
                ON docente_codirector.id = gr.codirector
            LEFT JOIN personas persona_codirector
                ON persona_codirector.id = docente_codirector.id_persona
            WHERE tg.id_estudiante = :idEstudiante
            """, nativeQuery = true)
    Optional<DirectorCodirectorResumen> findDirectorCodirectorByEstudianteId(@Param("idEstudiante") Long idEstudiante);

    interface DirectorCodirectorResumen {
        String getDirector();
        String getCodirector();
    }
}
