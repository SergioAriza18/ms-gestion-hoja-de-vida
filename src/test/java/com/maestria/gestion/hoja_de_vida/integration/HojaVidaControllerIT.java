package com.maestria.gestion.hoja_de_vida.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "/sql/hoja-vida-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/hoja-vida-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@SqlConfig(encoding = "UTF-8")
@DisplayName("Pruebas de integración de endpoints de hoja de vida")
class HojaVidaControllerIT {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    @DisplayName("Debe buscar estudiante por identificación")
    void buscarEstudiantePorIdentificacionRetornaCoincidencia() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", "987654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("2023002"))
                .andExpect(jsonPath("$[0].identificacion").value("987654321"));
    }

    @Test
    @DisplayName("Debe rechazar búsqueda sin valor")
    void buscarEstudianteSinValorRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/buscar")
                .param("valor", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("valor")));
    }

    @Test
    @DisplayName("Debe consultar historia académica del estudiante")
    void obtenerHistoriaAcademicaRetornaResumenCompleto() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "2024001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiante.codigoEstudiante").value("2024001"))
                .andExpect(jsonPath("$.estudiante.nombreCompleto").value("Laura Gómez"))
                .andExpect(jsonPath("$.estudiante.promedioCarrera").value(4.18))
                .andExpect(jsonPath("$.historiaAcademica.fundamentacion.asignaturas[0].notaDefinitiva").value("4.0"))
                .andExpect(jsonPath("$.historiaAcademica.electivas.asignaturas[0].notaDefinitiva").value("3.4"))
                .andExpect(jsonPath("$.historiaAcademica.investigacion.asignaturas[0].notaDefinitiva").value("A"))
                .andExpect(jsonPath("$.historiaAcademica.complementacion.competenciasEmpresariales.asignaturas[0].notaDefinitiva").value("3.5"))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.creditosCumplidos").value(14))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.tituloTesis").value("Sistema académico"))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.directorTesis").value("Diana Torres"))
                .andExpect(jsonPath("$.historiaAcademica.informacionAdicional.codirectorTesis").value("Andrés Ruiz"));
    }

    @Test
    @DisplayName("Debe retornar 404 cuando no existe historia académica")
    void obtenerHistoriaAcademicaCuandoNoExisteEstudianteRetornaNotFound() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "NO-EXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RESOURCE_NOT_FOUND"));
    }
}
