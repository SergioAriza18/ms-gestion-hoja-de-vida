package com.maestria.gestion.hoja_de_vida.unit.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.maestria.gestion.hoja_de_vida.client.GestionSolicitudesClient;
import com.maestria.gestion.hoja_de_vida.client.GestionSolicitudesResponseNormalizer;
import com.maestria.gestion.hoja_de_vida.config.GestionSolicitudesProperties;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCanceladaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaHomologadaDTO;

@DisplayName("Cliente de gestión de solicitudes")
class GestionSolicitudesClientTest {

    private static final String URL_BASE = "http://localhost:8095/msmaestriac/";
    private static final String URL_HOMOLOGACIONES =
            "http://localhost:8095/msmaestriac/gestionSolicitud/estudiantes/{idEstudiante}/asignaturas-homologadas";
    private static final String URL_CANCELACIONES =
            "http://localhost:8095/msmaestriac/gestionSolicitud/estudiantes/{idEstudiante}/asignaturas-canceladas";
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(2);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(7);

    private RestTemplateBuilder builder;
    private RestTemplate restTemplate;
    private GestionSolicitudesResponseNormalizer responseNormalizer;
    private GestionSolicitudesClient client;

    @BeforeEach
    void configurarCliente() {
        builder = mock(RestTemplateBuilder.class);
        restTemplate = mock(RestTemplate.class);
        responseNormalizer = mock(GestionSolicitudesResponseNormalizer.class);

        when(builder.setConnectTimeout(CONNECT_TIMEOUT)).thenReturn(builder);
        when(builder.setReadTimeout(READ_TIMEOUT)).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        GestionSolicitudesProperties properties = new GestionSolicitudesProperties();
        properties.setUrl(URL_BASE);
        properties.setConnectTimeout(CONNECT_TIMEOUT);
        properties.setReadTimeout(READ_TIMEOUT);

        client = new GestionSolicitudesClient(builder, properties, responseNormalizer);
    }

    @Test
    @DisplayName("Debe aplicar los tiempos de conexión y lectura configurados")
    void aplicaTimeoutsConfigurados() {
        verify(builder).setConnectTimeout(CONNECT_TIMEOUT);
        verify(builder).setReadTimeout(READ_TIMEOUT);
        verify(builder).build();
    }

    @Test
    @DisplayName("Debe consultar homologaciones en la URL esperada y normalizar su respuesta")
    void consultaYNormalizaHomologaciones() {
        AsignaturaHomologadaDTO recibida = AsignaturaHomologadaDTO.builder()
                .nombreAsignatura("  Arquitectura de software  ")
                .build();
        AsignaturaHomologadaDTO normalizada = AsignaturaHomologadaDTO.builder()
                .nombreAsignatura("Arquitectura de software")
                .build();
        AsignaturaHomologadaDTO[] respuesta = { recibida };
        List<AsignaturaHomologadaDTO> homologaciones = List.of(normalizada);

        when(restTemplate.getForObject(
                URL_HOMOLOGACIONES,
                AsignaturaHomologadaDTO[].class,
                1L))
                .thenReturn(respuesta);
        when(responseNormalizer.normalizarHomologaciones(respuesta)).thenReturn(homologaciones);

        assertThat(client.obtenerAsignaturasHomologadas(1L)).isSameAs(homologaciones);
        verify(responseNormalizer).normalizarHomologaciones(respuesta);
    }

    @Test
    @DisplayName("Debe retornar homologaciones vacías ante un error HTTP")
    void retornaHomologacionesVaciasAnteErrorHttp() {
        when(restTemplate.getForObject(
                anyString(),
                eq(AsignaturaHomologadaDTO[].class),
                eq(1L)))
                .thenThrow(new RestClientException("Error HTTP de prueba"));

        assertThat(client.obtenerAsignaturasHomologadas(1L)).isEmpty();
    }

    @Test
    @DisplayName("Debe consultar cancelaciones en la URL esperada y normalizar su respuesta")
    void consultaYNormalizaCancelaciones() {
        AsignaturaCanceladaDTO recibida = AsignaturaCanceladaDTO.builder()
                .nombreAsignatura("  Inteligencia artificial  ")
                .build();
        AsignaturaCanceladaDTO normalizada = AsignaturaCanceladaDTO.builder()
                .nombreAsignatura("Inteligencia artificial")
                .build();
        AsignaturaCanceladaDTO[] respuesta = { recibida };
        List<AsignaturaCanceladaDTO> cancelaciones = List.of(normalizada);

        when(restTemplate.getForObject(
                URL_CANCELACIONES,
                AsignaturaCanceladaDTO[].class,
                1L))
                .thenReturn(respuesta);
        when(responseNormalizer.normalizarCancelaciones(respuesta)).thenReturn(cancelaciones);

        assertThat(client.obtenerAsignaturasCanceladas(1L)).isSameAs(cancelaciones);
        verify(responseNormalizer).normalizarCancelaciones(respuesta);
    }

    @Test
    @DisplayName("Debe retornar cancelaciones vacías cuando solicitudes no está disponible")
    void retornaCancelacionesVaciasCuandoSolicitudesNoEstaDisponible() {
        when(restTemplate.getForObject(
                anyString(),
                eq(AsignaturaCanceladaDTO[].class),
                eq(1L)))
                .thenThrow(new ResourceAccessException("Conexión rechazada"));

        assertThat(client.obtenerAsignaturasCanceladas(1L)).isEmpty();
    }
}
