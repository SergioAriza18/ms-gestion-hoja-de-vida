package com.maestria.gestion.hoja_de_vida.mapper;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.Persona;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteResponseDTO;

public class EstudianteMapper {

    private EstudianteMapper() {
    }

    public static EstudianteResponseDTO toResponseDTO(Estudiante estudiante) {
        Persona persona = estudiante.getPersona();

        return EstudianteResponseDTO.builder()
                .codigo(estudiante.getCodigo())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .tipoIdentificacion(persona.getTipoIdentificacion())
                .identificacion(persona.getIdentificacion())
                .build();
    }
}
