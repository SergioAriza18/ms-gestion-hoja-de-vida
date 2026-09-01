package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maestria.gestion.hoja_de_vida.domain.Pasantia;

public interface PasantiaRepository extends JpaRepository<Pasantia, Long> {

    List<Pasantia> findAllByIdEstudiante(Long idEstudiante);
}
