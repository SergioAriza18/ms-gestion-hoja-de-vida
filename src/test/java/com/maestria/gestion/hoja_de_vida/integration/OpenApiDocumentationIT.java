package com.maestria.gestion.hoja_de_vida.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Documentación OpenAPI")
class OpenApiDocumentationIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debe publicar el contrato OpenAPI con autenticación JWT y los endpoints del módulo")
    void publicarContratoOpenApi() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/swagger-ui/index.html"));

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("API de Gestión de Hoja de Vida"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.type").value("http"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath(
                        "$.components.responses.BadRequest.content['application/json'].schema['$ref']")
                        .value("#/components/schemas/ApiError"))
                .andExpect(jsonPath(
                        "$.components.responses.BadRequest.content['application/json'].examples.error.value.estado")
                        .value(400))
                .andExpect(jsonPath(
                        "$.components.responses.BadRequest.content['application/json'].examples.error.value.codigo")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath(
                        "$.components.responses.Unauthorized.content['application/json'].examples.error.value.estado")
                        .value(401))
                .andExpect(jsonPath(
                        "$.components.responses.Unauthorized.content['application/json'].examples.error.value.codigo")
                        .value("UNAUTHORIZED"))
                .andExpect(jsonPath(
                        "$.components.responses.Forbidden.content['application/json'].examples.error.value.estado")
                        .value(403))
                .andExpect(jsonPath(
                        "$.components.responses.NotFound.content['application/json'].examples.error.value.estado")
                        .value(404))
                .andExpect(jsonPath(
                        "$.components.responses.PayloadTooLarge.content['application/json'].examples.error.value.estado")
                        .value(413))
                .andExpect(jsonPath("$.security[0].bearerAuth").exists())
                .andExpect(jsonPath("$.paths['/api/hoja-vida/estudiantes'].get.responses['401']").exists())
                .andExpect(jsonPath("$.paths['/api/hoja-vida/estudiantes'].get.responses['403']").exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/historia-academica']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones'].post.requestBody.content['multipart/form-data']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones'].post.responses['201']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones'].post.responses['400']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones'].post.requestBody.description")
                        .value("Datos de la distinción y resolución PDF obligatoria."))
                .andExpect(jsonPath(
                        "$.components.schemas.RegistrarDistincionRequestDTO.properties.resolucion.format")
                        .value("binary"))
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones/{tipo}'].put.requestBody.content['multipart/form-data']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones/{tipo}'].put.responses['204']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones/{tipo}'].delete.responses['204']")
                        .exists())
                .andExpect(jsonPath(
                        "$.paths['/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones/{tipo}/resolucion'].get.responses['200'].content['application/pdf'].schema.type")
                        .value("string"));
    }
}
