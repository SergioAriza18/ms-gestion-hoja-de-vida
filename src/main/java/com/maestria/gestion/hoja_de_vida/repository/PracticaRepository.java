package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maestria.gestion.hoja_de_vida.domain.Practica;

public interface PracticaRepository extends JpaRepository<Practica, Long> {

    @Query("""
            SELECT DISTINCT practica
      FROM Practica practica
      LEFT JOIN FETCH practica.actividades
      WHERE practica.idEstudiante = :idEstudiante
      ORDER BY practica.id ASC
      """)
    List<Practica> findAllByIdEstudiante(@Param("idEstudiante") Long idEstudiante);
}
