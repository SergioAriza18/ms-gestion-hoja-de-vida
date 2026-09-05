package com.maestria.gestion.hoja_de_vida.unit.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.Pasantia;
import com.maestria.gestion.hoja_de_vida.domain.Persona;
import com.maestria.gestion.hoja_de_vida.domain.Practica;
import com.maestria.gestion.hoja_de_vida.domain.Publicacion;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteDistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository.DirectorCodirectorResumen;
import com.maestria.gestion.hoja_de_vida.repository.PasantiaRepository;
import com.maestria.gestion.hoja_de_vida.repository.PracticaRepository;
import com.maestria.gestion.hoja_de_vida.repository.PublicacionRepository;
import com.maestria.gestion.hoja_de_vida.service.impl.HistoriaAcademicaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de HistoriaAcademicaServiceImpl")
class HistoriaAcademicaServiceImplTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private AsignaturaCursadaRepository asignaturaCursadaRepository;

    @Mock
    private PasantiaRepository pasantiaRepository;

    @Mock
    private PublicacionRepository publicacionRepository;

    @Mock
    private PracticaRepository practicaRepository;

    @Mock
    private EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;

    @InjectMocks
    private HistoriaAcademicaServiceImpl historiaAcademicaService;

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el estudiante no existe")
    void obtenerHistoriaAcademicaCuandoNoExisteEstudianteLanzaResourceNotFoundException() {
        when(estudianteRepository.findByCodigo("NO-EXISTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> historiaAcademicaService.obtenerHistoriaAcademica("NO-EXISTE"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("estudiante solicitado");
    }

    @Test
    @DisplayName("Debe construir la historia académica con créditos, promedio y áreas correctas")
    void obtenerHistoriaAcademicaConstruyeRespuestaCompleta() {
        Estudiante estudiante = estudiante();
        List<AsignaturaCursadaResumen> asignaturas = List.of(
                asignatura(5L, "M10001", "Fundamentos de computación", 4, new BigDecimal("4.0")),
                asignatura(6L, "M10002", "Electiva avanzada", 3, new BigDecimal("3.4")),
                asignatura(7L, "M27708", "Seminario de investigación", 4, BigDecimal.valueOf(5)),
                asignatura(8L, "M10003", "Competencias empresariales", 2, new BigDecimal("3.5")),
                asignatura(9L, "M27712", "Trabajo de grado II", 4, BigDecimal.valueOf(5)));

        when(estudianteRepository.findByCodigo("2024001")).thenReturn(Optional.of(estudiante));
        when(asignaturaCursadaRepository.findAsignaturasResumenByEstudianteId(1L)).thenReturn(asignaturas);
        when(pasantiaRepository.findAllByIdEstudiante(1L)).thenReturn(List.of(
                Pasantia.builder().creditosAsignados(2).acta("ACT-1").build(),
                Pasantia.builder().creditosAsignados(-1).acta("ACT-2").build()));
        when(publicacionRepository.findAllByIdEstudiante(1L)).thenReturn(List.of(
                Publicacion.builder().creditosAsignados(1).nombrePublicacion("Artículo").tipoPublicacion("Revista")
                        .build()));
        when(practicaRepository.findAllByIdEstudiante(1L)).thenReturn(List.of(
                Practica.builder().creditosAsignados(1).acta("ACT-3").build()));
        when(estudianteRepository.findTituloTesisByEstudianteId(1L)).thenReturn(Optional.of("Sistema académico"));
        when(estudianteRepository.findDirectorCodirectorByEstudianteId(1L))
                .thenReturn(Optional.of(directorCodirector("Diana Torres", "Andrés Ruiz")));

        when(estudianteDistincionAcademicaRepository.findCodigosByEstudianteId(1L)).thenReturn(List.of());

        HistoriaAcademicaResponseDTO resultado = historiaAcademicaService.obtenerHistoriaAcademica("2024001");

        assertThat(resultado.getEstudiante().getCodigoEstudiante()).isEqualTo("2024001");
        assertThat(resultado.getEstudiante().getNombreCompleto()).isEqualTo("Laura Gómez");
        assertThat(resultado.getEstudiante().getPromedioCarrera()).isEqualByComparingTo(new BigDecimal("3.6"));

        assertThat(resultado.getHistoriaAcademica().getFundamentacion().getAsignaturas()).hasSize(1);
        assertThat(resultado.getHistoriaAcademica().getElectivas().getAsignaturas()).hasSize(1);
        assertThat(resultado.getHistoriaAcademica().getInvestigacion().getAsignaturas()).hasSize(1);
        assertThat(resultado.getHistoriaAcademica().getComplementacion().getCompetenciasEmpresariales()
                .getAsignaturas()).hasSize(1);
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getAsignaturas()).hasSize(1);

        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getCreditosCumplidos()).isEqualTo(14);
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getTituloTesis())
                .isEqualTo("Sistema académico");
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getDirectorTesis())
                .isEqualTo("Diana Torres");
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getCodirectorTesis())
                .isEqualTo("Andrés Ruiz");
    }

    @Test
    @DisplayName("Debe retornar promedio nulo y textos vacíos cuando no hay información adicional")
    void obtenerHistoriaAcademicaSinNotasNiInformacionAdicionalRetornaValoresPorDefecto() {
        Estudiante estudiante = estudiante();
        when(estudianteRepository.findByCodigo("2024001")).thenReturn(Optional.of(estudiante));
        when(asignaturaCursadaRepository.findAsignaturasResumenByEstudianteId(1L)).thenReturn(List.of());
        when(pasantiaRepository.findAllByIdEstudiante(1L)).thenReturn(List.of());
        when(publicacionRepository.findAllByIdEstudiante(1L)).thenReturn(List.of());
        when(practicaRepository.findAllByIdEstudiante(1L)).thenReturn(List.of());
        when(estudianteRepository.findTituloTesisByEstudianteId(1L)).thenReturn(Optional.empty());
        when(estudianteRepository.findDirectorCodirectorByEstudianteId(1L)).thenReturn(Optional.empty());

        when(estudianteDistincionAcademicaRepository.findCodigosByEstudianteId(1L)).thenReturn(List.of());

        HistoriaAcademicaResponseDTO resultado = historiaAcademicaService.obtenerHistoriaAcademica("2024001");

        assertThat(resultado.getEstudiante().getPromedioCarrera()).isNull();
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getCreditosCumplidos()).isZero();
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getTituloTesis()).isEmpty();
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getDirectorTesis()).isEmpty();
        assertThat(resultado.getHistoriaAcademica().getInformacionAdicional().getCodirectorTesis()).isEmpty();
    }

    @Test
    @DisplayName("Debe redondear el promedio a un decimal")
    void consultarPromedioCarreraRedondeaAUnDecimal() {
        when(asignaturaCursadaRepository.findAsignaturasResumenByEstudianteId(1L))
                .thenReturn(List.of(asignatura(
                        5L,
                        "M10001",
                        "Fundamentos de computación",
                        4,
                        new BigDecimal("3.47"))))
                .thenReturn(List.of(asignatura(
                        6L,
                        "M10002",
                        "Electiva avanzada",
                        3,
                        new BigDecimal("4.56"))));

        assertThat(historiaAcademicaService.consultarPromedioCarrera(1L))
                .isEqualByComparingTo(new BigDecimal("3.5"));
        assertThat(historiaAcademicaService.consultarPromedioCarrera(1L))
                .isEqualByComparingTo(new BigDecimal("4.6"));
    }

    private Estudiante estudiante() {
        return Estudiante.builder()
                .id(1L)
                .codigo("2024001")
                .correoUniversidad("laura.gomez@universidad.edu")
                .periodoIngreso("2024-1")
                .semestreAcademico(2)
                .persona(Persona.builder()
                        .nombre("Laura")
                        .apellido("Gómez")
                        .identificacion(123456789L)
                        .build())
                .build();
    }

    private AsignaturaCursadaResumen asignatura(Long areaFormacion, String codigo, String nombre, Integer creditos,
            BigDecimal nota) {
        return new AsignaturaCursadaResumen() {
            @Override
            public Integer getAnio() {
                return 2024;
            }

            @Override
            public Integer getPeriodo() {
                return 1;
            }

            @Override
            public String getCodigoAsignatura() {
                return codigo;
            }

            @Override
            public String getNombreAsignatura() {
                return nombre;
            }

            @Override
            public Integer getCreditos() {
                return creditos;
            }

            @Override
            public BigDecimal getNota() {
                return nota;
            }

            @Override
            public Long getAreaFormacion() {
                return areaFormacion;
            }
        };
    }

    private DirectorCodirectorResumen directorCodirector(String director, String codirector) {
        return new DirectorCodirectorResumen() {
            @Override
            public String getDirector() {
                return director;
            }

            @Override
            public String getCodirector() {
                return codirector;
            }
        };
    }
}
