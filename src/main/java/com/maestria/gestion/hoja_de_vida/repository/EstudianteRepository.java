package com.maestria.gestion.hoja_de_vida.repository;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCodigo(String codigo);

    Optional<Estudiante> findByPersonaIdentificacion(String identificacion);

    List<Estudiante> findByPersonaNombreStartingWithIgnoreCase(String nombre);
}
