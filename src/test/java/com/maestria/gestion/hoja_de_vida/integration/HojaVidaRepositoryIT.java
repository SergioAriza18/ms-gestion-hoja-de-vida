package com.maestria.gestion.hoja_de_vida.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.transaction.annotation.Transactional;

import com.maestria.gestion.hoja_de_vida.domain.Publicacion;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteDistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.repository.PublicacionRepository;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.CODIGO_SUFICIENCIA_IDIOMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_APROBATORIA;

@SpringBootTest
@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "/sql/hoja-vida-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/hoja-vida-common-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/hoja-vida-academic-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@SqlConfig(encoding = "UTF-8")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Transactional
@DisplayName("Pruebas de integración de consultas específicas de hoja de vida")
class HojaVidaRepositoryIT {

    @Autowired
    private AsignaturaCursadaRepository asignaturaCursadaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql(scripts = "/sql/hoja-vida-filter-data.sql")
    @DisplayName("Debe filtrar estudiantes por cumplimiento de suficiencia en idioma extranjero")
    void findAllBySuficienciaIdiomaRetornaAprobadosYPendientes() {
        var aprobados = estudianteRepository.findAllBySuficienciaIdioma(
                true, null, CODIGO_SUFICIENCIA_IDIOMA, NOTA_APROBATORIA);
        var pendientes = estudianteRepository.findAllBySuficienciaIdioma(
                false, null, CODIGO_SUFICIENCIA_IDIOMA, NOTA_APROBATORIA);

        assertThat(aprobados)
                .extracting(estudiante -> estudiante.getCodigo())
                .containsExactly("2024001");
        assertThat(pendientes)
                .extracting(estudiante -> estudiante.getCodigo())
                .containsExactly("2023002", "2022003");
    }

    @Test
    @DisplayName("Debe consultar el resumen de asignaturas definitivas")
    void findAsignaturasResumenByEstudianteIdRetornaSoloAsignaturasDefinitivas() {
        List<AsignaturaCursadaResumen> asignaturas = asignaturaCursadaRepository
                .findAsignaturasResumenByEstudianteId(1L);

        assertThat(asignaturas).hasSize(6);
        assertThat(asignaturas)
                .extracting(AsignaturaCursadaResumen::getCodigoAsignatura)
                .containsExactly("M10001", "M10002", "M10003", "M27708", "M27709", "M27712")
                .doesNotContain("M99999");

        AsignaturaCursadaResumen primeraAsignatura = asignaturas.get(0);
        assertThat(primeraAsignatura.getAnio()).isEqualTo(2024);
        assertThat(primeraAsignatura.getPeriodo()).isEqualTo(1);
        assertThat(primeraAsignatura.getNombreAsignatura()).isEqualTo("Fundamentos de computación");
        assertThat(primeraAsignatura.getCreditos()).isEqualTo(4);
        assertThat(primeraAsignatura.getNota()).isEqualByComparingTo(new BigDecimal("4.0"));
        assertThat(primeraAsignatura.getAreaFormacion()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Debe consultar el título de tesis del estudiante")
    void findTituloTesisByEstudianteIdRetornaTitulo() {
        assertThat(estudianteRepository.findTituloTesisByEstudianteId(1L))
                .contains("Sistema académico");
    }

    @Test
    @DisplayName("Debe consultar director y codirector del estudiante")
    void findDirectorCodirectorByEstudianteIdRetornaNombresCompletos() {
        assertThat(estudianteRepository.findDirectorCodirectorByEstudianteId(1L))
                .get()
                .satisfies(resumen -> {
                    assertThat(resumen.getDirector()).isEqualTo("Diana Torres");
                    assertThat(resumen.getCodirector()).isEqualTo("Andrés Ruiz");
                });
    }

    @Test
    @DisplayName("Debe conservar el director cuando no existe codirector")
    void findDirectorCodirectorByEstudianteIdSinCodirectorRetornaDirector() {
        assertThat(estudianteRepository.findDirectorCodirectorByEstudianteId(2L))
                .get()
                .satisfies(resumen -> {
                    assertThat(resumen.getDirector()).isEqualTo("Diana Torres");
                    assertThat(resumen.getCodirector()).isNull();
                });
    }

    @Test
    @DisplayName("Debe consultar publicaciones mediante la relación con estudiantes")
    void findAllByIdEstudianteRetornaPublicacionesAsociadas() {
        List<Publicacion> publicaciones = publicacionRepository.findAllByIdEstudiante(1L);

        assertThat(publicaciones).hasSize(2);
        assertThat(publicaciones)
                .extracting(Publicacion::getNombrePublicacion)
                .containsExactly("Artículo de investigación", "Publicación sin créditos");
        assertThat(publicaciones.get(0).getUrlPublicacion()).isEqualTo("https://example.test/publicacion");
        assertThat(publicaciones.get(1).getCreditosAsignados()).isNull();
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe consultar distinciones y resoluciones asociadas al estudiante")
    void findDistincionesByEstudianteRetornaCodigosOrdenadosYResolucion() {
        assertThat(estudianteDistincionAcademicaRepository.findCodigosByEstudianteId(1L))
                .containsExactly("EXCELENCIA_ACADEMICA", "MENCION_HONOR_TRABAJO_GRADO");
        assertThat(estudianteDistincionAcademicaRepository
                .existsByEstudianteIdAndDistincionId(1L, 2L))
                .isTrue();
        assertThat(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2024001", "MENCION_HONOR_TRABAJO_GRADO"))
                .get()
                .satisfies(registro -> {
                    assertThat(registro.getNumeroResolucion()).isEqualTo("RES-MEN-001");
                    assertThat(registro.getResolucionPdf()).containsExactly(
                            "%PDF-1.4\nmencion".getBytes(StandardCharsets.UTF_8));
                });
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe impedir distinciones duplicadas para un mismo estudiante")
    void insertarDistincionDuplicadaViolaRestriccionUnica() {
        assertThatThrownBy(() -> jdbcTemplate.update("""
                INSERT INTO estudiantes_distinciones_academicas
                    (id, id_estudiante, id_distincion_academica,
                     numero_resolucion, fecha_resolucion, resolucion_pdf)
                VALUES
                    (3, 1, 1, 'RES-DUP-001', DATE '2025-03-10', X'255044462D312E34')
                """))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
