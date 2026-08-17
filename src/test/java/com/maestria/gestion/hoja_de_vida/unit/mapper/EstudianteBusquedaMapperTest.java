package com.maestria.gestion.hoja_de_vida.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.Persona;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.mapper.EstudianteBusquedaMapper;

@DisplayName("Pruebas unitarias de EstudianteBusquedaMapper")
class EstudianteBusquedaMapperTest {

    @Test
    @DisplayName("Debe mapear los datos básicos del estudiante")
    void toResponseDtoMapeaDatosBasicosDelEstudiante() {
        Estudiante estudiante = Estudiante.builder()
                .codigo("2023002")
                .periodoIngreso("2023-2")
                .semestreAcademico(4)
                .persona(Persona.builder()
                        .nombre("Carlos")
                        .apellido("Pérez")
                        .identificacion(987654321L)
                        .build())
                .build();

        EstudianteBusquedaDTO resultado = EstudianteBusquedaMapper.toResponseDTO(estudiante);

        assertThat(resultado.getCodigo()).isEqualTo("2023002");
        assertThat(resultado.getNombre()).isEqualTo("Carlos");
        assertThat(resultado.getApellido()).isEqualTo("Pérez");
        assertThat(resultado.getPeriodoIngreso()).isEqualTo("2023-2");
        assertThat(resultado.getIdentificacion()).isEqualTo("987654321");
        assertThat(resultado.getSemestreActual()).isEqualTo(4);
    }

    @Test
    @DisplayName("Debe retornar identificación nula cuando la persona no tiene identificación")
    void toResponseDtoRetornaIdentificacionNulaCuandoPersonaNoTieneIdentificacion() {
        Estudiante estudiante = Estudiante.builder()
                .codigo("2023003")
                .periodoIngreso("2023-2")
                .persona(Persona.builder()
                        .nombre("Ana")
                        .apellido("López")
                        .identificacion(null)
                        .build())
                .build();

        EstudianteBusquedaDTO resultado = EstudianteBusquedaMapper.toResponseDTO(estudiante);

        assertThat(resultado.getIdentificacion()).isNull();
    }
}
