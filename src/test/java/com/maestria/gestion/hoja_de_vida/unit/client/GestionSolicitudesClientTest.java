package com.maestria.gestion.hoja_de_vida.unit.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.maestria.gestion.hoja_de_vida.client.GestionSolicitudesClient;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCanceladaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaHomologadaDTO;

class GestionSolicitudesClientTest {

    @Test
    void retornaListaVaciaCuandoSolicitudesNoEstaDisponible() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);

        when(builder.setConnectTimeout(any(Duration.class))).thenReturn(builder);
        when(builder.setReadTimeout(any(Duration.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        when(restTemplate.getForObject(
                anyString(),
                eq(AsignaturaHomologadaDTO[].class),
                eq(1L)))
                .thenThrow(new ResourceAccessException("Conexión rechazada"));

        GestionSolicitudesClient client = new GestionSolicitudesClient(builder, "http://localhost:8095/msmaestriac/");

        List<AsignaturaHomologadaDTO> resultado = client.obtenerAsignaturasHomologadas(1L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void retornaCancelacionesVaciasCuandoSolicitudesNoEstaDisponible() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);

        when(builder.setConnectTimeout(any(Duration.class))).thenReturn(builder);
        when(builder.setReadTimeout(any(Duration.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        when(restTemplate.getForObject(
                anyString(),
                eq(AsignaturaCanceladaDTO[].class),
                eq(1L)))
                .thenThrow(new ResourceAccessException("Conexión rechazada"));

        GestionSolicitudesClient client = new GestionSolicitudesClient(builder, "http://localhost:8095/msmaestriac/");

        assertThat(client.obtenerAsignaturasCanceladas(1L)).isEmpty();
    }
}
