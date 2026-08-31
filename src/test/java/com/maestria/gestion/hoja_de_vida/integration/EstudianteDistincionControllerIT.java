package com.maestria.gestion.hoja_de_vida.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

import com.maestria.gestion.hoja_de_vida.repository.EstudianteDistincionAcademicaRepository;

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
@DisplayName("Pruebas de integración de distinciones académicas")
class EstudianteDistincionControllerIT {

    private static final byte[] PDF_PRUEBA = "%PDF-1.4\nresolucion-prueba"
            .getBytes(StandardCharsets.UTF_8);
    private static final byte[] PDF_MENCION_REGISTRADA = "%PDF-1.4\nmencion"
            .getBytes(StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;

    @Test
    @DisplayName("Debe registrar una mención de honor con su resolución PDF")
    void registrarMencionHonorGuardaDistincionYResolucion() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2023002")
                .file(resolucionPdf())
                .param("tipo", "MENCION_HONOR_TRABAJO_GRADO")
                .param("numeroResolucion", "  RES-MEN-010  ")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isCreated());

        assertThat(estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo("2023002", "MENCION_HONOR_TRABAJO_GRADO"))
                .get()
                .satisfies(registro -> {
                    assertThat(registro.getNumeroResolucion()).isEqualTo("RES-MEN-010");
                    assertThat(registro.getFechaResolucion()).isEqualTo(LocalDate.of(2025, 3, 10));
                    assertThat(registro.getResolucionPdf()).containsExactly(PDF_PRUEBA);
                });
    }

    @Test
    @Sql(statements = "UPDATE matricula_calificaciones SET nota = 4.8 "
            + "WHERE id_matricula = 1 AND es_definitiva = TRUE")
    @DisplayName("Debe registrar excelencia académica cuando el promedio es 4.8")
    void registrarExcelenciaConPromedioMinimoGuardaDistincion() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2024001")
                .file(resolucionPdf())
                .param("tipo", "EXCELENCIA_ACADEMICA")
                .param("numeroResolucion", "RES-EXC-010")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isCreated());

        assertThat(estudianteDistincionAcademicaRepository
                .existsByEstudianteIdAndDistincionId(1L, 1L))
                .isTrue();
    }

    @Test
    @DisplayName("Debe rechazar excelencia académica cuando el promedio es inferior a 4.8")
    void registrarExcelenciaSinPromedioMinimoRetornaBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2024001")
                .file(resolucionPdf())
                .param("tipo", "EXCELENCIA_ACADEMICA")
                .param("numeroResolucion", "RES-EXC-011")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("4.8")));

        assertThat(estudianteDistincionAcademicaRepository
                .existsByEstudianteIdAndDistincionId(1L, 1L))
                .isFalse();
    }

    @Test
    @DisplayName("Debe rechazar excelencia académica cuando el estudiante no tiene promedio")
    void registrarExcelenciaSinPromedioRetornaBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2022003")
                .file(resolucionPdf())
                .param("tipo", "EXCELENCIA_ACADEMICA")
                .param("numeroResolucion", "RES-EXC-012")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("4.8")));

        assertThat(estudianteDistincionAcademicaRepository
                .existsByEstudianteIdAndDistincionId(3L, 1L))
                .isFalse();
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe rechazar una distinción ya registrada")
    void registrarDistincionDuplicadaRetornaBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2024001")
                .file(resolucionPdf())
                .param("tipo", "MENCION_HONOR_TRABAJO_GRADO")
                .param("numeroResolucion", "RES-MEN-002")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("ya tiene registrada")));
    }

    @Test
    @DisplayName("Debe rechazar un archivo cuyo tipo de contenido no es PDF")
    void registrarDistincionConTipoDeArchivoInvalidoRetornaBadRequest() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile(
                "resolucion", "resolucion.txt", MediaType.TEXT_PLAIN_VALUE, PDF_PRUEBA);

        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2023002")
                .file(archivo)
                .param("tipo", "MENCION_HONOR_TRABAJO_GRADO")
                .param("numeroResolucion", "RES-MEN-012")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("debe ser un PDF")));
    }

    @Test
    @DisplayName("Debe rechazar un archivo sin la firma de un PDF")
    void registrarDistincionConContenidoInvalidoRetornaBadRequest() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile(
                "resolucion", "resolucion.pdf", MediaType.APPLICATION_PDF_VALUE,
                "contenido-invalido".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2023002")
                .file(archivo)
                .param("tipo", "MENCION_HONOR_TRABAJO_GRADO")
                .param("numeroResolucion", "RES-MEN-013")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("PDF válido")));
    }

    @Test
    @DisplayName("Debe rechazar el registro cuando falta la resolución PDF")
    void registrarDistincionSinArchivoRetornaBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2023002")
                .param("tipo", "MENCION_HONOR_TRABAJO_GRADO")
                .param("numeroResolucion", "RES-MEN-014")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("resolucion")))
                .andExpect(jsonPath("$.mensaje", containsString("obligatorio")));
    }

    @Test
    @DisplayName("Debe rechazar una fecha de resolución futura")
    void registrarDistincionConFechaFuturaRetornaBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2023002")
                .file(resolucionPdf())
                .param("tipo", "MENCION_HONOR_TRABAJO_GRADO")
                .param("numeroResolucion", "RES-MEN-015")
                .param("fechaResolucion", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("no puede ser futura")));
    }

    @Test
    @DisplayName("Debe retornar 404 al registrar una distinción para un estudiante inexistente")
    void registrarDistincionParaEstudianteInexistenteRetornaNotFound() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "NO-EXISTE")
                .file(resolucionPdf())
                .param("tipo", "MENCION_HONOR_TRABAJO_GRADO")
                .param("numeroResolucion", "RES-MEN-016")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.mensaje", containsString("estudiante")));
    }

    @Test
    @DisplayName("Debe rechazar un tipo de distinción inexistente")
    void registrarDistincionConTipoInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2023002")
                .file(resolucionPdf())
                .param("tipo", "DISTINCION_INEXISTENTE")
                .param("numeroResolucion", "RES-MEN-017")
                .param("fechaResolucion", "2025-03-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.mensaje", containsString("tipo")));
    }

    @Test
    @DisplayName("Debe rechazar un contenido que no sea multipart")
    void registrarDistincionSinContenidoMultipartRetornaUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/api/hoja-vida/estudiantes/{codigo}/distinciones", "2023002")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.codigo").value("UNSUPPORTED_MEDIA_TYPE"));
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe consultar la resolución PDF de una distinción registrada")
    void obtenerResolucionDistincionRetornaPdfEnLinea() throws Exception {
        mockMvc.perform(get(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}/resolucion",
                "2024001",
                "MENCION_HONOR_TRABAJO_GRADO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(PDF_MENCION_REGISTRADA))
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"resolucion-mencion_honor_trabajo_grado.pdf\""))
                .andExpect(header().string(
                        HttpHeaders.CONTENT_LENGTH,
                        String.valueOf(PDF_MENCION_REGISTRADA.length)));
    }

    @Test
    @DisplayName("Debe retornar 404 cuando la distinción no tiene resolución registrada")
    void obtenerResolucionDistincionNoRegistradaRetornaNotFound() throws Exception {
        mockMvc.perform(get(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}/resolucion",
                "2024001",
                "MENCION_HONOR_TRABAJO_GRADO"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.mensaje", containsString("resolución")));
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe incluir las distinciones otorgadas en la historia académica")
    void obtenerHistoriaAcademicaIncluyeDistincionesOtorgadas() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes/{codigo}/historia-academica", "2024001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.historiaAcademica.informacionAdicional.distincionesAcademicas",
                        hasSize(2)))
                .andExpect(jsonPath(
                        "$.historiaAcademica.informacionAdicional.distincionesAcademicas[0]")
                        .value("EXCELENCIA_ACADEMICA"))
                .andExpect(jsonPath(
                        "$.historiaAcademica.informacionAdicional.distincionesAcademicas[1]")
                        .value("MENCION_HONOR_TRABAJO_GRADO"));
    }

    private MockMultipartFile resolucionPdf() {
        return new MockMultipartFile(
                "resolucion",
                "resolucion.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                PDF_PRUEBA);
    }

}

