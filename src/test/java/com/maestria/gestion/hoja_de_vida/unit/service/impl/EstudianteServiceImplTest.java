package com.maestria.gestion.hoja_de_vida.unit.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.EstudianteDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.domain.Persona;
import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteDistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.impl.EstudianteServiceImpl;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.CODIGO_SUFICIENCIA_IDIOMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_APROBATORIA;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de EstudianteServiceImpl")
class EstudianteServiceImplTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;

    @InjectMocks
    private EstudianteServiceImpl estudianteService;

    @Test
    @DisplayName("Debe listar estudiantes ordenados por período de ingreso descendente")
    void listarDebeConsultarEstudiantesOrdenadosPorPeriodoIngresoDescendente() {
        Estudiante estudiante = estudiante("2024001", "Laura", "Gómez", 123456789L, "2024-1");
        when(estudianteRepository.findAll(any(Sort.class))).thenReturn(List.of(estudiante));

        List<EstudianteBusquedaDTO> resultado = estudianteService.listar();

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(estudianteRepository).findAll(sortCaptor.capture());
        Sort.Order orden = sortCaptor.getValue().getOrderFor("periodoIngreso");

        assertThat(orden).isNotNull();
        assertThat(orden.getDirection()).isEqualTo(Sort.Direction.DESC);
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("2024001");
    }

    @Test
    @DisplayName("Debe retornar el estudiante cuando el criterio coincide con el código")
    void buscarCuandoCoincideCodigoRetornaEstudiante() {
        Estudiante estudiante = estudiante("2024001", "Laura", "Gómez", 123456789L, "2024-1");
        when(estudianteRepository.findByCodigo("2024001")).thenReturn(Optional.of(estudiante));

        List<EstudianteBusquedaDTO> resultado = estudianteService.buscar("2024001");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("2024001");
        assertThat(resultado.get(0).getNombre()).isEqualTo("Laura");
        verify(estudianteRepository).findByCodigo("2024001");
    }

    @Test
    @DisplayName("Debe buscar por identificación cuando el criterio es numérico")
    void buscarCuandoCriterioEsNumericoBuscaPorIdentificacion() {
        Estudiante estudiante = estudiante("2023002", "Carlos", "Pérez", 987654321L, "2023-2");
        when(estudianteRepository.findByCodigo("987654321")).thenReturn(Optional.empty());
        when(estudianteRepository.findByPersonaIdentificacion(987654321L)).thenReturn(Optional.of(estudiante));

        List<EstudianteBusquedaDTO> resultado = estudianteService.buscar("987654321");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdentificacion()).isEqualTo("987654321");
        verify(estudianteRepository).findByPersonaIdentificacion(987654321L);
    }

    @Test
    @DisplayName("Debe buscar por nombre cuando no encuentra coincidencias por código ni identificación")
    void buscarCuandoNoCoincideCodigoNiIdentificacionBuscaPorNombre() {
        Estudiante estudiante = estudiante("2022003", "María", "Rodríguez", 456789123L, "2022-1");
        when(estudianteRepository.findByCodigo("Mar")).thenReturn(Optional.empty());
        when(estudianteRepository.findAllByPersonaNombreStartingWithIgnoreCase(any(String.class), any(Sort.class)))
                .thenReturn(List.of(estudiante));

        List<EstudianteBusquedaDTO> resultado = estudianteService.buscar("Mar");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("María");
        verify(estudianteRepository).findAllByPersonaNombreStartingWithIgnoreCase(any(String.class), any(Sort.class));
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando el criterio solo contiene espacios")
    void buscarCuandoCriterioEstaVacioRetornaListaVacia() {
        List<EstudianteBusquedaDTO> resultado = estudianteService.buscar("   ");

        assertThat(resultado).isEmpty();
        verifyNoInteractions(estudianteRepository);
    }

    @Test
    @DisplayName("Debe continuar con búsqueda por nombre cuando la identificación supera el rango de Long")
    void buscarCuandoIdentificacionSuperaRangoDeLongBuscaPorNombre() {
        String criterio = "999999999999999999999999999999";
        when(estudianteRepository.findByCodigo(criterio)).thenReturn(Optional.empty());
        when(estudianteRepository.findAllByPersonaNombreStartingWithIgnoreCase(any(String.class), any(Sort.class)))
                .thenReturn(List.of());

        List<EstudianteBusquedaDTO> resultado = estudianteService.buscar(criterio);

        assertThat(resultado).isEmpty();
        verify(estudianteRepository).findAllByPersonaNombreStartingWithIgnoreCase(any(String.class), any(Sort.class));
    }

    @Test
    @DisplayName("Debe filtrar estudiantes con suficiencia de idioma aprobada")
    void filtrarPorSuficienciaAprobadaRetornaCoincidencias() {
        Estudiante estudiante = estudiante("2024001", "Laura", "Apellido", 123456789L, "2024-1");
        estudiante.setSemestreAcademico(2);
        when(estudianteRepository.findAllBySuficienciaIdioma(
                eq(true), isNull(), eq(CODIGO_SUFICIENCIA_IDIOMA), eq(NOTA_APROBATORIA)))
                .thenReturn(List.of(estudiante));

        List<EstudianteBusquedaDTO> resultado = estudianteService.filtrar(true, null);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("2024001");
        assertThat(resultado.get(0).getSemestreActual()).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe filtrar estudiantes por semestre actual")
    void filtrarPorSemestreActualRetornaCoincidencias() {
        Estudiante estudiante = estudiante("2023002", "Carlos", "Apellido", 123456789L, "2023-2");
        estudiante.setSemestreAcademico(4);
        when(estudianteRepository.findAllBySemestreAcademico(eq(4), any(Sort.class)))
                .thenReturn(List.of(estudiante));

        List<EstudianteBusquedaDTO> resultado = estudianteService.filtrar(null, 4);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getSemestreActual()).isEqualTo(4);
    }

    @Test
    @DisplayName("Debe rechazar la consulta cuando no se indica ningún filtro")
    void filtrarSinCriteriosLanzaExcepcion() {
        assertThatThrownBy(() -> estudianteService.filtrar(null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Debe indicar al menos un filtro.");

        verifyNoInteractions(estudianteRepository);
    }

    @Test
    @DisplayName("Debe retornar el PDF de una distinción registrada")
    void obtenerResolucionDistincionRegistradaRetornaPdf() {
        byte[] pdf = "%PDF-prueba".getBytes();
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(EstudianteDistincionAcademica.builder()
                        .resolucionPdf(pdf)
                        .build()));

        byte[] resultado = estudianteService.obtenerResolucionDistincion(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA);

        assertThat(resultado).isSameAs(pdf);
    }

    @Test
    @DisplayName("Debe informar cuando una distinción no tiene resolución registrada")
    void obtenerResolucionDistincionNoRegistradaLanzaExcepcion() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "MENCION_HONOR_TRABAJO_GRADO"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> estudianteService.obtenerResolucionDistincion(
                "2024001",
                TipoDistincionAcademica.MENCION_HONOR_TRABAJO_GRADO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No se encontró la resolución de la distinción solicitada.");
    }

    @Test
    @DisplayName("Debe retornar los datos guardados de una distinción")
    void obtenerDetalleDistincionRegistradaRetornaDatos() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(EstudianteDistincionAcademica.builder()
                        .numeroResolucion("RES-EXC-001")
                        .fechaResolucion(LocalDate.of(2025, 1, 15))
                        .build()));

        var resultado = estudianteService.obtenerDetalleDistincion(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA);

        assertThat(resultado.getTipo()).isEqualTo(TipoDistincionAcademica.EXCELENCIA_ACADEMICA);
        assertThat(resultado.getNumeroResolucion()).isEqualTo("RES-EXC-001");
        assertThat(resultado.getFechaResolucion()).isEqualTo(LocalDate.of(2025, 1, 15));
    }

    @Test
    @DisplayName("Debe editar los datos de la distinción y conservar el PDF existente")
    void editarDistincionSinNuevoPdfActualizaDatosYConservaResolucion() {
        byte[] pdfOriginal = "%PDF-original".getBytes();
        EstudianteDistincionAcademica registro = EstudianteDistincionAcademica.builder()
                .numeroResolucion("RES-ANTERIOR")
                .fechaResolucion(LocalDate.of(2025, 1, 15))
                .resolucionPdf(pdfOriginal)
                .build();
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(registro));

        estudianteService.editarDistincion(
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
    void editarDistincionConNumeroVacioLanzaExcepcion() {
        assertThatThrownBy(() -> estudianteService.editarDistincion(
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
    void editarDistincionNoRegistradaLanzaExcepcion() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> estudianteService.editarDistincion(
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
    void eliminarDistincionRegistradaEliminaAsociacion() {
        EstudianteDistincionAcademica registro = EstudianteDistincionAcademica.builder().id(1L).build();
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.of(registro));

        estudianteService.eliminarDistincion(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA);

        verify(estudianteDistincionAcademicaRepository).delete(registro);
    }

    @Test
    @DisplayName("Debe informar cuando se intenta eliminar una distinción no registrada")
    void eliminarDistincionNoRegistradaLanzaExcepcion() {
        when(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "EXCELENCIA_ACADEMICA"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> estudianteService.eliminarDistincion(
                "2024001",
                TipoDistincionAcademica.EXCELENCIA_ACADEMICA))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No se encontró la distinción académica solicitada para el estudiante.");

        verify(estudianteDistincionAcademicaRepository, never()).delete(any());
    }

    private Estudiante estudiante(String codigo, String nombre, String apellido, Long identificacion,
            String periodoIngreso) {
        return Estudiante.builder()
                .codigo(codigo)
                .periodoIngreso(periodoIngreso)
                .persona(Persona.builder()
                        .nombre(nombre)
                        .apellido(apellido)
                        .identificacion(identificacion)
                        .build())
                .build();
    }

}
