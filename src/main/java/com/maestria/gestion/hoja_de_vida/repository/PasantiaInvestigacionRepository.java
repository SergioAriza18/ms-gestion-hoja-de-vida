package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maestria.gestion.hoja_de_vida.domain.PasantiaInvestigacion;

public interface PasantiaInvestigacionRepository extends JpaRepository<PasantiaInvestigacion, Long> {

    List<PasantiaInvestigacion> findByIdEstudiante(Long idEstudiante);
}
