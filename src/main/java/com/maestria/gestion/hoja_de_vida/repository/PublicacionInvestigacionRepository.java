package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maestria.gestion.hoja_de_vida.domain.PublicacionInvestigacion;

public interface PublicacionInvestigacionRepository extends JpaRepository<PublicacionInvestigacion, Long> {

    List<PublicacionInvestigacion> findByIdEstudiante(Long idEstudiante);
}
