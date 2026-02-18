package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maestria.gestion.hoja_de_vida.domain.PracticaDocente;

public interface PracticaDocenteRepository extends JpaRepository<PracticaDocente, Long> {

    List<PracticaDocente> findByIdEstudiante(Long idEstudiante);
}
