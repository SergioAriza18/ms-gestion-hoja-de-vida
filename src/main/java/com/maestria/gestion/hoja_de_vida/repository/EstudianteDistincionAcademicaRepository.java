package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maestria.gestion.hoja_de_vida.domain.EstudianteDistincionAcademica;

public interface EstudianteDistincionAcademicaRepository
        extends JpaRepository<EstudianteDistincionAcademica, Long> {

    boolean existsByEstudianteIdAndDistincionId(Long idEstudiante, Long idDistincion);

    Optional<EstudianteDistincionAcademica> findByEstudianteCodigoAndDistincionCodigo(
            String codigoEstudiante,
            String codigoDistincion);

    @Query("""
            SELECT distincion.codigo
            FROM EstudianteDistincionAcademica estudianteDistincion
            JOIN estudianteDistincion.distincion distincion
            WHERE estudianteDistincion.estudiante.id = :idEstudiante
            ORDER BY distincion.codigo
            """)
    List<String> findCodigosByEstudianteId(@Param("idEstudiante") Long idEstudiante);
}
