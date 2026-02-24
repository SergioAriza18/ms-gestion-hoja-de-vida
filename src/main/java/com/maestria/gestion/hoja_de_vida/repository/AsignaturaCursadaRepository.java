package com.maestria.gestion.hoja_de_vida.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maestria.gestion.hoja_de_vida.domain.AsignaturaCursada;

public interface AsignaturaCursadaRepository extends JpaRepository<AsignaturaCursada, Long> {

    @Query(value = """
            SELECT
                matricula.anio AS anio,
                matricula.periodo AS periodo,
                asignatura.codigo_asignatura AS codigoAsignatura,
                asignatura.nombre_asignatura AS nombreAsignatura,
                asignatura.creditos AS creditos,
                calificacion.nota AS nota,
                asignatura.area_formacion AS areaFormacion
            FROM matricula_calificaciones calificacion
            INNER JOIN matriculas matricula
                ON matricula.id = calificacion.id_matricula
            INNER JOIN asignaturas asignatura
                ON asignatura.id = calificacion.id_asignatura
            WHERE matricula.id_estudiante = :idEstudiante
              AND (calificacion.es_definitiva = 1 OR calificacion.es_definitiva IS NULL)
            """, nativeQuery = true)
    List<AsignaturaCursadaResumen> findAsignaturasResumenByEstudianteId(@Param("idEstudiante") Long idEstudiante);

    interface AsignaturaCursadaResumen {
        Integer getAnio();
        Integer getPeriodo();
        Long getCodigoAsignatura();
        String getNombreAsignatura();
        Integer getCreditos();
        BigDecimal getNota();
        Long getAreaFormacion();
    }
}
