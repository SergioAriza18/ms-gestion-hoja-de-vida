package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maestria.gestion.hoja_de_vida.domain.Practica;

public interface PracticaRepository extends JpaRepository<Practica, Long> {

    List<Practica> findAllByIdEstudiante(Long idEstudiante);
}
