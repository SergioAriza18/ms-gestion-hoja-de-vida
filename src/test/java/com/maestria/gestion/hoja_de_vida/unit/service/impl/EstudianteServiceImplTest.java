package com.maestria.gestion.hoja_de_vida.unit.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import com.maestria.gestion.hoja_de_vida.domain.Persona;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.impl.EstudianteServiceImpl;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.CODIGO_SUFICIENCIA_IDIOMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_APROBATORIA;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de EstudianteServiceImpl")
class EstudianteServiceImplTest {

    @Mock
    private EstudianteRepository estudianteRepository;

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
