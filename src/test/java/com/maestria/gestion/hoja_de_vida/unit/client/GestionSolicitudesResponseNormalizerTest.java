package com.maestria.gestion.hoja_de_vida.unit.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.maestria.gestion.hoja_de_vida.client.GestionSolicitudesResponseNormalizer;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCanceladaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaHomologadaDTO;

@DisplayName("Normalización de respuestas de gestión de solicitudes")
class GestionSolicitudesResponseNormalizerTest {

    private final GestionSolicitudesResponseNormalizer normalizer = new GestionSolicitudesResponseNormalizer();

    @Test
    @DisplayName("Debe normalizar todos los campos de una homologación")
    void normalizaTodosLosCamposDeHomologacion() {
        AsignaturaHomologadaDTO asignatura = AsignaturaHomologadaDTO.builder()
                .nombreAsignatura("  Arquitectura de software  ")
                .creditos(4)
                .calificacion(4.5)
                .programaProcedencia("  Especialización en desarrollo  ")
                .institucionProcedencia("  Universidad de origen  ")
                .build();

        assertThat(normalizer.normalizarHomologaciones(new AsignaturaHomologadaDTO[] { asignatura }))
                .singleElement()
                .satisfies(resultado -> {
                    assertThat(resultado.getNombreAsignatura()).isEqualTo("Arquitectura de software");
                    assertThat(resultado.getCreditos()).isEqualTo(4);
                    assertThat(resultado.getCalificacion()).isEqualTo(4.5);
                    assertThat(resultado.getProgramaProcedencia()).isEqualTo("Especialización en desarrollo");
                    assertThat(resultado.getInstitucionProcedencia()).isEqualTo("Universidad de origen");
                });
    }

    @Test
    @DisplayName("Debe retornar homologaciones vacías cuando la respuesta no tiene cuerpo")
    void retornaHomologacionesVaciasCuandoRespuestaEsNula() {
        assertThat(normalizer.normalizarHomologaciones(null)).isEmpty();
    }

    @Test
    @DisplayName("Debe omitir homologaciones sin nombre y limpiar valores inválidos")
    void validaRegistrosDeHomologaciones() {
        AsignaturaHomologadaDTO[] respuesta = {
                null,
                AsignaturaHomologadaDTO.builder().nombreAsignatura("   ").build(),
                AsignaturaHomologadaDTO.builder()
                        .nombreAsignatura("  Sistemas distribuidos ")
                        .creditos(-2)
                        .calificacion(Double.NaN)
                        .programaProcedencia(" ")
                        .build(),
                AsignaturaHomologadaDTO.builder()
                        .nombreAsignatura("Bases de datos")
                        .creditos(0)
                        .calificacion(5.1)
                        .build()
        };

        assertThat(normalizer.normalizarHomologaciones(respuesta))
                .hasSize(2)
                .allSatisfy(resultado -> {
                    assertThat(resultado.getCreditos()).isNull();
                    assertThat(resultado.getCalificacion()).isNull();
                })
                .first()
                .satisfies(resultado -> {
                    assertThat(resultado.getNombreAsignatura()).isEqualTo("Sistemas distribuidos");
                    assertThat(resultado.getProgramaProcedencia()).isNull();
                });
    }

    @Test
    @DisplayName("Debe conservar los límites válidos de la escala de calificación")
    void conservaCalificacionesEnLosLimitesValidos() {
        AsignaturaHomologadaDTO[] respuesta = {
                AsignaturaHomologadaDTO.builder()
                        .nombreAsignatura("Asignatura con nota mínima")
                        .calificacion(0.0)
                        .build(),
                AsignaturaHomologadaDTO.builder()
                        .nombreAsignatura("Asignatura con nota máxima")
                        .calificacion(5.0)
                        .build()
        };

        assertThat(normalizer.normalizarHomologaciones(respuesta))
                .extracting(AsignaturaHomologadaDTO::getCalificacion)
                .containsExactly(0.0, 5.0);
    }

    @Test
    @DisplayName("Debe normalizar todos los campos de una cancelación")
    void normalizaTodosLosCamposDeCancelacion() {
        AsignaturaCanceladaDTO asignatura = AsignaturaCanceladaDTO.builder()
                .nombreAsignatura("  Inteligencia artificial  ")
                .grupo("  A  ")
                .periodoCancelacion("  2026-2  ")
                .build();

        assertThat(normalizer.normalizarCancelaciones(new AsignaturaCanceladaDTO[] { asignatura }))
                .singleElement()
                .satisfies(resultado -> {
                    assertThat(resultado.getNombreAsignatura()).isEqualTo("Inteligencia artificial");
                    assertThat(resultado.getGrupo()).isEqualTo("A");
                    assertThat(resultado.getPeriodoCancelacion()).isEqualTo("2026-2");
                });
    }

    @Test
    @DisplayName("Debe retornar cancelaciones vacías cuando la respuesta no tiene cuerpo")
    void retornaCancelacionesVaciasCuandoRespuestaEsNula() {
        assertThat(normalizer.normalizarCancelaciones(null)).isEmpty();
    }

    @Test
    @DisplayName("Debe omitir cancelaciones sin nombre o con periodo inválido")
    void validaRegistrosDeCancelaciones() {
        AsignaturaCanceladaDTO[] respuesta = {
                null,
                AsignaturaCanceladaDTO.builder()
                        .nombreAsignatura(" ")
                        .periodoCancelacion("2026-1")
                        .build(),
                AsignaturaCanceladaDTO.builder()
                        .nombreAsignatura("Inteligencia artificial")
                        .periodoCancelacion("2026-3")
                        .build(),
                AsignaturaCanceladaDTO.builder()
                        .nombreAsignatura("Arquitectura")
                        .periodoCancelacion(" ")
                        .build(),
                AsignaturaCanceladaDTO.builder()
                        .nombreAsignatura("  Bases de datos  ")
                        .grupo(" ")
                        .periodoCancelacion(" 2025-1 ")
                        .build()
        };

        assertThat(normalizer.normalizarCancelaciones(respuesta))
                .singleElement()
                .satisfies(resultado -> {
                    assertThat(resultado.getNombreAsignatura()).isEqualTo("Bases de datos");
                    assertThat(resultado.getGrupo()).isNull();
                    assertThat(resultado.getPeriodoCancelacion()).isEqualTo("2025-1");
                });
    }
}
