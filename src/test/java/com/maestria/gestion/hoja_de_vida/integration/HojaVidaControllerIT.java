package com.maestria.gestion.hoja_de_vida.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "/sql/hoja-vida-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/hoja-vida-common-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/hoja-vida-academic-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@SqlConfig(encoding = "UTF-8")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@DisplayName("Pruebas de integración de endpoints de hoja de vida")
class HojaVidaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // Caso: listado general de estudiantes respetando el orden por período de ingreso descendente.
    @Test
    @DisplayName("Debe listar estudiantes ordenados por período de ingreso")
    void listarEstudiantesRetornaEstudiantesOrdenados() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("2024001"))
                .andExpect(jsonPath("$[0].nombre").value("Laura"))
                .andExpect(jsonPath("$[0].periodoIngreso").value("2024-1"))
                .andExpect(jsonPath("$[1].codigo").value("2023002"));
    }

    // Caso: búsqueda exacta por identificación cuando no hay coincidencia previa por código.
    @Test
    @DisplayName("Debe buscar estudiante por identificación")
    void buscarEstudiantePorIdentificacionRetornaCoincidencia() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", "987654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("2023002"))
                .andExpect(jsonPath("$[0].identificacion").value("987654321"));
    }

    // Caso: búsqueda exacta por código de estudiante.
    @Test
    @DisplayName("Debe buscar estudiante por código")
    void buscarEstudiantePorCodigoRetornaCoincidencia() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", "2024001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigo").value("2024001"))
                .andExpect(jsonPath("$[0].nombre").value("Laura"));
    }

    // Caso: búsqueda por prefijo del nombre del estudiante.
    @Test
    @DisplayName("Debe buscar estudiantes por nombre parcial")
    void buscarEstudiantePorNombreParcialRetornaCoincidencias() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", "Car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigo").value("2023002"))
                .andExpect(jsonPath("$[0].nombre").value("Carlos"));
    }

    // Caso: búsqueda válida sin resultados debe responder 200 con lista vacía.
    @Test
    @DisplayName("Debe retornar lista vacía cuando la búsqueda no tiene coincidencias")
    void buscarEstudianteSinCoincidenciasRetornaListaVacia() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", "SinCoincidencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    // Caso: parámetro de búsqueda en blanco debe activar validación y responder 400.
    @Test
    @DisplayName("Debe rechazar búsqueda sin valor")
    void buscarEstudianteSinValorRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("valor")));
    }

    // Caso: parámetro de búsqueda superior al tamaño permitido debe responder 400.
    @Test
    @DisplayName("Debe rechazar búsqueda con valor mayor a 100 caracteres")
    void buscarEstudianteConValorMayorACienCaracteresRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", "A".repeat(101)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("100 caracteres")));
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-filter-data.sql")
    @DisplayName("Debe filtrar estudiantes con suficiencia de idioma aprobada o pendiente")
    void filtrarEstudiantesPorSuficienciaIdiomaRetornaCoincidencias() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/filtrar")
                .param("suficienciaIdiomaAprobada", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigo").value("2024001"))
                .andExpect(jsonPath("$[0].semestreActual").value(2));

        mockMvc.perform(get("/api/hoja-vida/estudiantes/filtrar")
                .param("suficienciaIdiomaAprobada", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].codigo").value("2023002"))
                .andExpect(jsonPath("$[1].codigo").value("2022003"));
    }

    @Test
    @DisplayName("Debe filtrar estudiantes por semestre actual")
    void filtrarEstudiantesPorSemestreActualRetornaCoincidencias() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/filtrar")
                .param("semestreActual", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigo").value("2023002"))
                .andExpect(jsonPath("$[0].semestreActual").value(4));
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-filter-data.sql")
    @DisplayName("Debe combinar los filtros de suficiencia y semestre actual")
    void filtrarEstudiantesCombinandoCriteriosRetornaCoincidencias() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/filtrar")
                .param("suficienciaIdiomaAprobada", "false")
                .param("semestreActual", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigo").value("2022003"));
    }

    @Test
    @DisplayName("Debe rechazar la consulta cuando no se indica ningún filtro")
    void filtrarEstudiantesSinCriteriosRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/filtrar"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("al menos un filtro")));
    }

    @Test
    @DisplayName("Debe rechazar un semestre actual no positivo")
    void filtrarEstudiantesConSemestreNoPositivoRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/filtrar")
                .param("semestreActual", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("mayor que cero")));
    }

    // Caso: consulta exitosa de historia académica con datos académicos e información adicional.
    @Test
    @DisplayName("Debe consultar historia académica del estudiante")
    void obtenerHistoriaAcademicaRetornaResumenCompleto() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "2024001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiante.codigoEstudiante").value("2024001"))
                .andExpect(jsonPath("$.estudiante.nombreCompleto").value("Laura Gómez"))
                .andExpect(jsonPath("$.estudiante.promedioCarrera").value(4.05))
                .andExpect(jsonPath("$.historiaAcademica.fundamentacion.asignaturas[0].notaDefinitiva").value("4.0"))
                .andExpect(jsonPath("$.historiaAcademica.electivas.asignaturas[0].notaDefinitiva").value("3.4"))
                .andExpect(jsonPath("$.historiaAcademica.investigacion.asignaturas[0].notaDefinitiva").value("A"))
                .andExpect(jsonPath("$.historiaAcademica.complementacion.competenciasEmpresariales.asignaturas[0].notaDefinitiva").value("3.5"))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.creditosCumplidos").value(14))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.tituloTesis").value("Sistema académico"))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.directorTesis").value("Diana Torres"))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.codirectorTesis").value("Andrés Ruiz"));
    }

    // Caso: historia académica debe mapear reglas especiales y omitir calificaciones no definitivas.
    @Test
    @DisplayName("Debe aplicar reglas especiales y excluir asignaturas no definitivas")
    void obtenerHistoriaAcademicaAplicaReglasEspecialesYExcluyeNoDefinitivas() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "2024001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.historiaAcademica.investigacion.asignaturas[*].notaDefinitiva", hasItem("NA")))
                .andExpect(jsonPath("$.historiaAcademica.fundamentacion.asignaturas[*].codigoMateria",
                        not(hasItem("M99999"))))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.creditosCumplidos").value(14));
    }

    // Caso: estudiante existente sin historia asociada debe retornar valores por defecto.
    @Test
    @DisplayName("Debe consultar historia académica sin información adicional")
    void obtenerHistoriaAcademicaSinInformacionAdicionalRetornaValoresPorDefecto() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "2022003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiante.codigoEstudiante").value("2022003"))
                .andExpect(jsonPath("$.estudiante.promedioCarrera").value(0))
                .andExpect(jsonPath("$.historiaAcademica.fundamentacion.asignaturas", hasSize(0)))
                .andExpect(jsonPath("$.historiaAcademica.electivas.asignaturas", hasSize(0)))
                .andExpect(jsonPath("$.historiaAcademica.investigacion.asignaturas", hasSize(0)))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.creditosCumplidos").value(0))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.tituloTesis").value(""))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.directorTesis").value(""))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.codirectorTesis").value(""));
    }

    // Caso: estudiante inexistente para historia académica debe responder 404.
    @Test
    @DisplayName("Debe retornar 404 cuando no existe historia académica")
    void obtenerHistoriaAcademicaCuandoNoExisteEstudianteRetornaNotFound() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "NO-EXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RESOURCE_NOT_FOUND"));
    }

    // Caso: código de estudiante con caracteres no permitidos debe responder 400.
    @Test
    @DisplayName("Debe rechazar código de estudiante con formato inválido")
    void obtenerHistoriaAcademicaConCodigoInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "ABC_123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("formato")));
    }

    // Caso: ruta no registrada debe integrarse con el manejador global y responder 404.
    @Test
    @DisplayName("Debe retornar 404 cuando la ruta no existe")
    void rutaInexistenteRetornaNotFound() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RESOURCE_NOT_FOUND"));
    }

    // Caso: método HTTP no soportado por el endpoint debe responder 405.
    @Test
    @DisplayName("Debe retornar 405 cuando el método HTTP no está permitido")
    void metodoNoPermitidoRetornaMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/api/hoja-vida/estudiantes"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.codigo").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.mensaje", containsString("POST")));
    }

    @Test
    @DisplayName("Debe rechazar filtros con formato inválido")
    void filtrarEstudiantesConFormatoInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/filtrar")
                .param("semestreActual", "cuarto"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("semestreActual")));
    }

}
