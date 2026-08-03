package com.maestria.gestion.hoja_de_vida.mapper;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.Persona;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;

public class EstudianteBusquedaMapper {

    private EstudianteBusquedaMapper() {
    }

    public static EstudianteBusquedaDTO toResponseDTO(Estudiante estudiante) {
        Persona persona = estudiante.getPersona();

        return EstudianteBusquedaDTO.builder()
                .codigo(estudiante.getCodigo())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .periodoIngreso(estudiante.getPeriodoIngreso())
                .identificacion(toStringOrNull(persona.getIdentificacion()))
                .build();
    }

    private static String toStringOrNull(Long value) {
        return value == null ? null : value.toString();
    }
}
