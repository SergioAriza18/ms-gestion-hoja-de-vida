package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maestria.gestion.hoja_de_vida.domain.AsignaturaCursada;

public interface AsignaturaCursadaRepository extends JpaRepository<AsignaturaCursada, Long> {

    List<AsignaturaCursada> findByIdEstudiante(Long idEstudiante);
}
