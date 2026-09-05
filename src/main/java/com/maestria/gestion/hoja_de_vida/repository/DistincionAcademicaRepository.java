package com.maestria.gestion.hoja_de_vida.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maestria.gestion.hoja_de_vida.domain.DistincionAcademica;

public interface DistincionAcademicaRepository extends JpaRepository<DistincionAcademica, Long> {

    Optional<DistincionAcademica> findByCodigo(String codigo);
}
