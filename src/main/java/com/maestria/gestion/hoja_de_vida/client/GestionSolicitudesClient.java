package com.maestria.gestion.hoja_de_vida.client;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.maestria.gestion.hoja_de_vida.config.GestionSolicitudesProperties;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCanceladaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaHomologadaDTO;

@Component
public class GestionSolicitudesClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionSolicitudesClient.class);
    private static final String RUTA_HOMOLOGACIONES =
            "/gestionSolicitud/estudiantes/{idEstudiante}/asignaturas-homologadas";
    private static final String RUTA_CANCELACIONES =
            "/gestionSolicitud/estudiantes/{idEstudiante}/asignaturas-canceladas";

    private final RestTemplate restTemplate;
    private final GestionSolicitudesResponseNormalizer responseNormalizer;
    private final String urlBase;

    public GestionSolicitudesClient(
            RestTemplateBuilder restTemplateBuilder,
            GestionSolicitudesProperties properties,
            GestionSolicitudesResponseNormalizer responseNormalizer) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(properties.getConnectTimeout())
                .setReadTimeout(properties.getReadTimeout())
                .build();
        this.responseNormalizer = responseNormalizer;
        this.urlBase = quitarBarraFinal(properties.getUrl());
    }

    public List<AsignaturaHomologadaDTO> obtenerAsignaturasHomologadas(Long idEstudiante) {
        return consultarAsignaturas(
                idEstudiante,
                RUTA_HOMOLOGACIONES,
                AsignaturaHomologadaDTO[].class,
                responseNormalizer::normalizarHomologaciones,
                "homologadas");
    }

    public List<AsignaturaCanceladaDTO> obtenerAsignaturasCanceladas(Long idEstudiante) {
        return consultarAsignaturas(
                idEstudiante,
                RUTA_CANCELACIONES,
                AsignaturaCanceladaDTO[].class,
                responseNormalizer::normalizarCancelaciones,
                "canceladas");
    }

    private <T> List<T> consultarAsignaturas(
            Long idEstudiante,
            String ruta,
            Class<T[]> tipoRespuesta,
            Function<T[], List<T>> normalizador,
            String tipoAsignaturas) {
        try {
            T[] respuesta = restTemplate.getForObject(
                    urlBase + ruta,
                    tipoRespuesta,
                    idEstudiante);
            return normalizador.apply(respuesta);
        } catch (RestClientException ex) {
            LOGGER.warn(
                    "No fue posible consultar las asignaturas {} del estudiante {}. "
                            + "La historia académica continuará sin esta información: {}",
                    tipoAsignaturas,
                    idEstudiante,
                    ex.getMessage());
            return List.of();
        }
    }

    private static String quitarBarraFinal(String valor) {
        return valor != null && valor.endsWith("/")
                ? valor.substring(0, valor.length() - 1)
                : valor;
    }
}
