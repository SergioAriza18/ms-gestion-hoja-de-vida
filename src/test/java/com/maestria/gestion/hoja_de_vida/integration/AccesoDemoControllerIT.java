package com.maestria.gestion.hoja_de_vida.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = {
        "app.demo-auth.enabled=true",
        "app.demo-auth.student-code=2024001",
        "app.demo-auth.student-name=Estudiante de prueba"
})
@AutoConfigureMockMvc
@ActiveProfiles({"demo", "test"})
@SqlGroup({
        @Sql(scripts = "/sql/hoja-vida-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/hoja-vida-common-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/hoja-vida-academic-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@SqlConfig(encoding = "UTF-8")
@DisplayName("Pruebas de integración del acceso de demostración")
class AccesoDemoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Test
    @DisplayName("Debe publicar solamente los perfiles configurados")
    void listarPerfilesRetornaCoordinadorYEstudiante() throws Exception {
        mockMvc.perform(get("/api/demo/auth/perfiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].perfil").value("COORDINADOR"))
                .andExpect(jsonPath("$[0].rol").value("ROLE_COORDINADOR"))
                .andExpect(jsonPath("$[1].perfil").value("ESTUDIANTE"))
                .andExpect(jsonPath("$[1].rol").value("ROLE_ESTUDIANTE"))
                .andExpect(jsonPath("$[1].codigoAcademico").value("2024001"));
    }

    @Test
    @DisplayName("Debe emitir un JWT de coordinador válido para los endpoints protegidos")
    void crearSesionCoordinadorPermiteListarEstudiantes() throws Exception {
        String token = solicitarToken("COORDINADOR");

        mockMvc.perform(get("/api/hoja-vida/estudiantes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe emitir al estudiante sus claims de rol y código académico")
    void crearSesionEstudianteIncluyeClaimsEsperados() throws Exception {
        String token = solicitarToken("ESTUDIANTE");

        Jwt jwt = jwtDecoder.decode(token);

        assertThat(jwt.getClaimAsStringList("rol")).isEqualTo(List.of("ROLE_ESTUDIANTE"));
        assertThat(jwt.getClaimAsString("codigoAcademico")).isEqualTo("2024001");
        assertThat(jwt.getExpiresAt()).isAfter(jwt.getIssuedAt());
    }

    @Test
    @DisplayName("Debe permitir iniciar sesión desde un túnel temporal de Cloudflare")
    void crearSesionDesdeTunelCloudflare() throws Exception {
        String origin = "https://demo-hoja-vida.trycloudflare.com";

        mockMvc.perform(post("/api/demo/auth/token")
                .header(HttpHeaders.ORIGIN, origin)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"perfil\":\"COORDINADOR\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    private String solicitarToken(String perfil) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/demo/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"perfil\":\"" + perfil + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        return response.get("accessToken").asText();
    }
}
