package com.maestria.gestion.hoja_de_vida.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.transaction.annotation.Transactional;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.Pasantia;
import com.maestria.gestion.hoja_de_vida.domain.Practica;
import com.maestria.gestion.hoja_de_vida.domain.Publicacion;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.repository.PasantiaRepository;
import com.maestria.gestion.hoja_de_vida.repository.PracticaRepository;
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
@DisplayName("Pruebas de integración de repositorios de hoja de vida")
class HojaVidaRepositoryIT {

    @Autowired
    private AsignaturaCursadaRepository asignaturaCursadaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PasantiaRepository pasantiaRepository;

    @Autowired
    private PracticaRepository practicaRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    // Caso: consulta derivada debe encontrar estudiante por código exacto.
    @Test
    @DisplayName("Debe consultar estudiante por código")
    void findByCodigoCuandoExisteRetornaEstudiante() {
        assertThat(estudianteRepository.findByCodigo("2024001"))
                .get()
                .satisfies(estudiante -> {
                    assertThat(estudiante.getCodigo()).isEqualTo("2024001");
                    assertThat(estudiante.getPersona().getNombre()).isEqualTo("Laura");
                    assertThat(estudiante.getPersona().getApellido()).isEqualTo("Gómez");
                });
    }

    // Caso: consulta derivada debe retornar Optional vacío cuando el código no existe.
    @Test
    @DisplayName("Debe retornar vacío cuando no existe estudiante por código")
    void findByCodigoCuandoNoExisteRetornaVacio() {
        assertThat(estudianteRepository.findByCodigo("NO-EXISTE"))
                .isEmpty();
    }

    // Caso: consulta derivada debe encontrar estudiante por identificación de la persona.
    @Test
    @DisplayName("Debe consultar estudiante por identificación")
    void findByPersonaIdentificacionCuandoExisteRetornaEstudiante() {
        assertThat(estudianteRepository.findByPersonaIdentificacion(987654321L))
                .get()
                .satisfies(estudiante -> {
                    assertThat(estudiante.getCodigo()).isEqualTo("2023002");
                    assertThat(estudiante.getPersona().getNombre()).isEqualTo("Carlos");
                });
    }

    // Caso: consulta derivada debe retornar Optional vacío cuando la identificación no existe.
    @Test
    @DisplayName("Debe retornar vacío cuando no existe estudiante por identificación")
    void findByPersonaIdentificacionCuandoNoExisteRetornaVacio() {
        assertThat(estudianteRepository.findByPersonaIdentificacion(999999999L))
                .isEmpty();
    }

    // Caso: búsqueda por prefijo de nombre debe ignorar mayúsculas/minúsculas y ordenar por período.
    @Test
    @DisplayName("Debe consultar estudiantes por prefijo de nombre ignorando mayúsculas")
    void findAllByPersonaNombreStartingWithIgnoreCaseRetornaEstudiantesOrdenados() {
        List<Estudiante> estudiantes = estudianteRepository.findAllByPersonaNombreStartingWithIgnoreCase(
                "a", Sort.by(Sort.Direction.DESC, "periodoIngreso"));

        assertThat(estudiantes)
                .extracting(Estudiante::getCodigo)
                .containsExactly("2022003");
        assertThat(estudiantes.get(0).getPersona().getNombre()).isEqualTo("Ana");
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-filter-data.sql")
    @DisplayName("Debe filtrar estudiantes por cumplimiento de suficiencia en idioma extranjero")
    void findAllBySuficienciaIdiomaRetornaAprobadosYPendientes() {
        var aprobados = estudianteRepository.findAllBySuficienciaIdioma(
                true, null, CODIGO_SUFICIENCIA_IDIOMA, NOTA_APROBATORIA);
        var pendientes = estudianteRepository.findAllBySuficienciaIdioma(
                false, null, CODIGO_SUFICIENCIA_IDIOMA, NOTA_APROBATORIA);

        assertThat(aprobados)
                .extracting(Estudiante::getCodigo)
                .containsExactly("2024001");
        assertThat(pendientes)
                .extracting(Estudiante::getCodigo)
                .containsExactly("2023002", "2022003");

    }

    @Test
    @DisplayName("Debe filtrar estudiantes por semestre actual")
    void findAllBySemestreAcademicoRetornaCoincidencias() {
        var estudiantes = estudianteRepository.findAllBySemestreAcademico(
                4, Sort.by(Sort.Direction.DESC, "periodoIngreso"));

        assertThat(estudiantes).hasSize(1);
        assertThat(estudiantes.get(0).getCodigo()).isEqualTo("2023002");
        assertThat(estudiantes.get(0).getSemestreAcademico()).isEqualTo(4);
    }

    // Caso: consulta nativa de asignaturas debe mapear columnas y excluir calificaciones no definitivas.
    @Test
    @DisplayName("Debe consultar resumen de asignaturas definitivas del estudiante")
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

    // Caso: estudiante sin matrícula ni calificaciones debe retornar lista vacía.
    @Test
    @DisplayName("Debe retornar lista vacía cuando el estudiante no tiene asignaturas")
    void findAsignaturasResumenByEstudianteIdSinAsignaturasRetornaListaVacia() {
        List<AsignaturaCursadaResumen> asignaturas = asignaturaCursadaRepository
                .findAsignaturasResumenByEstudianteId(3L);

        assertThat(asignaturas).isEmpty();
    }

    // Caso: consulta nativa de trabajo de grado debe retornar título cuando existe.
    @Test
    @DisplayName("Debe consultar título de tesis del estudiante")
    void findTituloTesisByEstudianteIdCuandoExisteRetornaTitulo() {
        assertThat(estudianteRepository.findTituloTesisByEstudianteId(1L))
                .contains("Sistema académico");
    }

    // Caso: consulta nativa de trabajo de grado debe retornar Optional vacío cuando no existe.
    @Test
    @DisplayName("Debe retornar vacío cuando el estudiante no tiene título de tesis")
    void findTituloTesisByEstudianteIdCuandoNoExisteRetornaVacio() {
        assertThat(estudianteRepository.findTituloTesisByEstudianteId(3L))
                .isEmpty();
    }

    // Caso: consulta nativa debe armar nombres completos de director y codirector.
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

    // Caso: consulta nativa debe tolerar codirector nulo sin perder el director.
    @Test
    @DisplayName("Debe consultar director cuando no existe codirector")
    void findDirectorCodirectorByEstudianteIdSinCodirectorRetornaDirector() {
        assertThat(estudianteRepository.findDirectorCodirectorByEstudianteId(2L))
                .get()
                .satisfies(resumen -> {
                    assertThat(resumen.getDirector()).isEqualTo("Diana Torres");
                    assertThat(resumen.getCodirector()).isNull();
                });
    }

    // Caso: consulta nativa de publicaciones debe usar la tabla intermedia de estudiantes.
    @Test
    @DisplayName("Debe consultar publicaciones asociadas al estudiante")
    void findAllByIdEstudianteRetornaPublicacionesAsociadas() {
        List<Publicacion> publicaciones = publicacionRepository.findAllByIdEstudiante(1L);

        assertThat(publicaciones).hasSize(2);
        assertThat(publicaciones)
                .extracting(Publicacion::getNombrePublicacion)
                .containsExactly("Artículo de investigación", "Publicación sin créditos");
        assertThat(publicaciones.get(0).getUrlPublicacion()).isEqualTo("https://example.test/publicacion");
        assertThat(publicaciones.get(1).getCreditosAsignados()).isNull();
    }

    // Caso: estudiante sin asociación en la tabla intermedia debe retornar lista vacía.
    @Test
    @DisplayName("Debe retornar lista vacía cuando el estudiante no tiene publicaciones")
    void findAllByIdEstudianteSinPublicacionesRetornaListaVacia() {
        List<Publicacion> publicaciones = publicacionRepository.findAllByIdEstudiante(3L);

        assertThat(publicaciones).isEmpty();
    }

    // Caso: consulta derivada debe retornar pasantías asociadas al estudiante.
    @Test
    @DisplayName("Debe consultar pasantías asociadas al estudiante")
    void findAllPasantiasByIdEstudianteRetornaPasantiasAsociadas() {
        List<Pasantia> pasantias = pasantiaRepository.findAllByIdEstudiante(1L);

        assertThat(pasantias).hasSize(2);
        assertThat(pasantias)
                .extracting(Pasantia::getActa)
                .containsExactly("ACT-PAS-1", "ACT-PAS-2");
        assertThat(pasantias.get(0).getCreditosAsignados()).isEqualTo(2);
        assertThat(pasantias.get(1).getCreditosAsignados()).isEqualTo(-1);
    }

    // Caso: estudiante sin pasantías debe retornar lista vacía.
    @Test
    @DisplayName("Debe retornar lista vacía cuando el estudiante no tiene pasantías")
    void findAllPasantiasByIdEstudianteSinPasantiasRetornaListaVacia() {
        List<Pasantia> pasantias = pasantiaRepository.findAllByIdEstudiante(3L);

        assertThat(pasantias).isEmpty();
    }

    // Caso: consulta derivada debe retornar prácticas asociadas al estudiante.
    @Test
    @DisplayName("Debe consultar prácticas asociadas al estudiante")
    void findAllPracticasByIdEstudianteRetornaPracticasAsociadas() {
        List<Practica> practicas = practicaRepository.findAllByIdEstudiante(1L);

        assertThat(practicas).hasSize(2);
        assertThat(practicas)
                .extracting(Practica::getActa)
                .containsExactly("ACT-PRA-1", "ACT-PRA-2");
        assertThat(practicas.get(0).getHoras()).isEqualTo(64);
        assertThat(practicas.get(1).getCreditosAsignados()).isEqualTo(-1);
    }

    // Caso: estudiante sin prácticas debe retornar lista vacía.
    @Test
    @DisplayName("Debe retornar lista vacía cuando el estudiante no tiene prácticas")
    void findAllPracticasByIdEstudianteSinPracticasRetornaListaVacia() {
        List<Practica> practicas = practicaRepository.findAllByIdEstudiante(3L);

        assertThat(practicas).isEmpty();
    }

}
