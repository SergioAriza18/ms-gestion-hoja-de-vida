package com.maestria.gestion.hoja_de_vida.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

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
@DisplayName("Pruebas de integración de seguridad JWT")
class SeguridadControllerIT {

    private static final String SECRET_BASE64 =
            "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWYwMTIzNDU2Nzg5YWJjZGVmMDEyMzQ1Njc4OWFiY2RlZg==";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debe responder 401 cuando no se envía token")
    void listarSinTokenRetornaUnauthorized() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("Debe responder 401 cuando el token está manipulado")
    void listarConTokenManipuladoRetornaUnauthorized() throws Exception {
        String tokenManipulado = token(List.of("ROLE_COORDINADOR"), null) + "x";

        mockMvc.perform(get("/api/hoja-vida/estudiantes")
                .header(HttpHeaders.AUTHORIZATION, bearer(tokenManipulado)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("Debe responder 401 cuando el token está vencido")
    void listarConTokenVencidoRetornaUnauthorized() throws Exception {
        String tokenVencido = token(
                List.of("ROLE_COORDINADOR"),
                null,
                Instant.now().minusSeconds(60));

        mockMvc.perform(get("/api/hoja-vida/estudiantes")
                .header(HttpHeaders.AUTHORIZATION, bearer(tokenVencido)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("Debe permitir al coordinador listar estudiantes")
    void listarComoCoordinadorRetornaOk() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes")
                .header(HttpHeaders.AUTHORIZATION, bearer(token(List.of("ROLE_COORDINADOR"), null))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe impedir al estudiante listar todos los estudiantes")
    void listarComoEstudianteRetornaForbidden() throws Exception {
        mockMvc.perform(get("/api/hoja-vida/estudiantes")
                .header(HttpHeaders.AUTHORIZATION, bearer(token(List.of("ROLE_ESTUDIANTE"), "2024001"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("Debe permitir al estudiante consultar su propia historia académica")
    void consultarHistoriaPropiaComoEstudianteRetornaOk() throws Exception {
        mockMvc.perform(get(
                "/api/hoja-vida/estudiantes/{codigo}/historia-academica",
                "2024001")
                .header(HttpHeaders.AUTHORIZATION, bearer(token(List.of("ROLE_ESTUDIANTE"), "2024001"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiante.codigoEstudiante").value("2024001"));
    }

    @Test
    @DisplayName("Debe impedir al estudiante consultar la historia de otro estudiante")
    void consultarHistoriaAjenaComoEstudianteRetornaForbidden() throws Exception {
        mockMvc.perform(get(
                "/api/hoja-vida/estudiantes/{codigo}/historia-academica",
                "2023002")
                .header(HttpHeaders.AUTHORIZATION, bearer(token(List.of("ROLE_ESTUDIANTE"), "2024001"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("FORBIDDEN"));
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe permitir al coordinador consultar los datos de una distinción")
    void consultarDetalleDistincionComoCoordinadorRetornaOk() throws Exception {
        mockMvc.perform(get(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}",
                "2024001",
                "EXCELENCIA_ACADEMICA")
                .header(HttpHeaders.AUTHORIZATION, bearer(token(List.of("ROLE_COORDINADOR"), null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroResolucion").value("RES-EXC-001"));
    }

    @Test
    @DisplayName("Debe impedir al estudiante consultar los datos de una distinción")
    void consultarDetalleDistincionComoEstudianteRetornaForbidden() throws Exception {
        mockMvc.perform(get(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}",
                "2024001",
                "EXCELENCIA_ACADEMICA")
                .header(HttpHeaders.AUTHORIZATION,
                        bearer(token(List.of("ROLE_ESTUDIANTE"), "2024001"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("FORBIDDEN"));
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe permitir al coordinador editar una distinción")
    void editarDistincionComoCoordinadorRetornaNoContent() throws Exception {
        mockMvc.perform(multipart(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}",
                "2024001",
                "EXCELENCIA_ACADEMICA")
                .param("numeroResolucion", "RES-EXC-JWT")
                .param("fechaResolucion", "2025-04-20")
                .header(HttpHeaders.AUTHORIZATION, bearer(token(List.of("ROLE_COORDINADOR"), null)))
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe impedir al estudiante editar una distinción")
    void editarDistincionComoEstudianteRetornaForbidden() throws Exception {
        mockMvc.perform(multipart(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}",
                "2024001",
                "EXCELENCIA_ACADEMICA")
                .param("numeroResolucion", "RES-EXC-JWT")
                .param("fechaResolucion", "2025-04-20")
                .header(HttpHeaders.AUTHORIZATION,
                        bearer(token(List.of("ROLE_ESTUDIANTE"), "2024001")))
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("FORBIDDEN"));
    }

    @Test
    @Sql(scripts = "/sql/hoja-vida-distinction-data.sql")
    @DisplayName("Debe permitir al coordinador eliminar una distinción")
    void eliminarDistincionComoCoordinadorRetornaNoContent() throws Exception {
        mockMvc.perform(delete(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}",
                "2024001",
                "EXCELENCIA_ACADEMICA")
                .header(HttpHeaders.AUTHORIZATION, bearer(token(List.of("ROLE_COORDINADOR"), null))))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe impedir al estudiante eliminar una distinción")
    void eliminarDistincionComoEstudianteRetornaForbidden() throws Exception {
        mockMvc.perform(delete(
                "/api/hoja-vida/estudiantes/{codigo}/distinciones/{tipo}",
                "2024001",
                "EXCELENCIA_ACADEMICA")
                .header(HttpHeaders.AUTHORIZATION,
                        bearer(token(List.of("ROLE_ESTUDIANTE"), "2024001"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("FORBIDDEN"));
    }

    private String token(List<String> roles, String codigoAcademico) throws Exception {
        Instant now = Instant.now();
        return token(roles, codigoAcademico, now.plusSeconds(3600));
    }

    private String token(List<String> roles, String codigoAcademico, Instant expiration) throws Exception {
        Instant now = Instant.now();
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                .subject("usuario@unicauca.edu.co")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .claim("rol", roles);

        if (codigoAcademico != null) {
            claims.claim("codigoAcademico", codigoAcademico);
        }

        SignedJWT signedJwt = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS512),
                claims.build());
        signedJwt.sign(new MACSigner(Base64.getDecoder().decode(SECRET_BASE64)));
        return signedJwt.serialize();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
