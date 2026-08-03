package com.maestria.gestion.hoja_de_vida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maestria.gestion.hoja_de_vida.domain.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    @Query(value = """
            SELECT publicacion.*
            FROM estudiantes_publicacion estudiantePublicacion
            INNER JOIN publicaciones publicacion
                ON publicacion.id = estudiantePublicacion.idpublicacion
            WHERE estudiantePublicacion.id_estudiante = :idEstudiante
            """, nativeQuery = true)
    List<Publicacion> findAllByIdEstudiante(@Param("idEstudiante") Long idEstudiante);
}
