package com.maestria.gestion.hoja_de_vida.unit.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maestria.gestion.hoja_de_vida.config.ArchivoResolucionProperties;
import com.maestria.gestion.hoja_de_vida.domain.EstudianteDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;
import com.maestria.gestion.hoja_de_vida.repository.DistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteDistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.HistoriaAcademicaService;
import com.maestria.gestion.hoja_de_vida.service.impl.DistincionAcademicaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de DistincionAcademicaServiceImpl")
class DistincionAcademicaServiceImplTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private DistincionAcademicaRepository distincionAcademicaRepository;

    @Mock
    private EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;

    @Mock
    private HistoriaAcademicaService historiaAcademicaService;

    @Mock
    private ArchivoResolucionProperties archivoResolucionProperties;

    @InjectMocks
    private DistincionAcademicaServiceImpl distincionAcademicaService;

    @Test
    @DisplayName("Debe retornar el PDF de una distinción registrada")
    void obtenerResolucionRegistradaRetornaPdf() {
        byte[] pdf = "%PDF-prueba".getBytes();
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(EstudianteDistincionAcademica.builder()
                        .numeroResolucion("RES-EXC-001")
                        .resolucionPdf(pdf)
                        .build()));

        var resultado = distincionAcademicaService.obtenerResolucion(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA);

        assertThat(resultado.getNombreArchivo()).isEqualTo("RES-EXC-001.pdf");
        assertThat(resultado.getContenido()).isSameAs(pdf);
    }

    @Test
    @DisplayName("Debe informar cuando una distinción no tiene resolución registrada")
    void obtenerResolucionNoRegistradaLanzaExcepcion() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "MENCION_HONOR_TRABAJO_GRADO"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> distincionAcademicaService.obtenerResolucion(
                "2024001",
                TipoDistincionAcademica.MENCION_HONOR_TRABAJO_GRADO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No se encontró la resolución de la distinción solicitada.");
    }

    @Test
    @DisplayName("Debe retornar los datos guardados de una distinción")
    void obtenerDetalleRegistradoRetornaDatos() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(EstudianteDistincionAcademica.builder()
                        .numeroResolucion("RES-EXC-001")
                        .fechaResolucion(LocalDate.of(2025, 1, 15))
                        .build()));

        var resultado = distincionAcademicaService.obtenerDetalle(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA);

        assertThat(resultado.getTipo()).isEqualTo(TipoDistincionAcademica.EXCELENCIA_ACADEMICA);
        assertThat(resultado.getNumeroResolucion()).isEqualTo("RES-EXC-001");
        assertThat(resultado.getFechaResolucion()).isEqualTo(LocalDate.of(2025, 1, 15));
    }

    @Test
    @DisplayName("Debe editar los datos de la distinción y conservar el PDF existente")
    void editarSinNuevoPdfActualizaDatosYConservaResolucion() {
        byte[] pdfOriginal = "%PDF-original".getBytes();
        EstudianteDistincionAcademica registro = EstudianteDistincionAcademica.builder()
                .numeroResolucion("RES-ANTERIOR")
                .fechaResolucion(LocalDate.of(2025, 1, 15))
                .resolucionPdf(pdfOriginal)
                .build();
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(registro));

        distincionAcademicaService.editar(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA,
                "  RES-ACTUALIZADA  ",
                LocalDate.of(2025, 4, 20),
                null);

        assertThat(registro.getNumeroResolucion()).isEqualTo("RES-ACTUALIZADA");
        assertThat(registro.getFechaResolucion()).isEqualTo(LocalDate.of(2025, 4, 20));
        assertThat(registro.getResolucionPdf()).isSameAs(pdfOriginal);
        verify(estudianteDistincionAcademicaRepository).saveAndFlush(registro);
    }

    @Test
    @DisplayName("Debe rechazar la edición cuando el número de resolución está vacío")
    void editarConNumeroVacioLanzaExcepcion() {
        assertThatThrownBy(() -> distincionAcademicaService.editar(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA,
                "   ",
                LocalDate.of(2025, 4, 20),
                null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El número de resolución es obligatorio.");

        verifyNoInteractions(estudianteDistincionAcademicaRepository);
    }

    @Test
    @DisplayName("Debe informar cuando se intenta editar una distinción no registrada")
    void editarNoRegistradaLanzaExcepcion() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> distincionAcademicaService.editar(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA,
                "RES-ACTUALIZADA",
                LocalDate.of(2025, 4, 20),
                null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No se encontró la distinción académica solicitada para el estudiante.");

        verify(estudianteDistincionAcademicaRepository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Debe eliminar una distinción registrada")
    void eliminarRegistradaEliminaAsociacion() {
        EstudianteDistincionAcademica registro = EstudianteDistincionAcademica.builder().id(1L).build();
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(registro));

        distincionAcademicaService.eliminar(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA);

        verify(estudianteDistincionAcademicaRepository).delete(registro);
    }

    @Test
    @DisplayName("Debe informar cuando se intenta eliminar una distinción no registrada")
    void eliminarNoRegistradaLanzaExcepcion() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> distincionAcademicaService.eliminar(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No se encontró la distinción académica solicitada para el estudiante.");

        verify(estudianteDistincionAcademicaRepository, never()).delete(any());
    }
}
