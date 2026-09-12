package com.maestria.gestion.hoja_de_vida.client;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaHomologadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCanceladaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.DocumentoFirmadoSolicitudDTO;
import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;

@Component
public class GestionSolicitudesClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionSolicitudesClient.class);

    private final RestTemplate restTemplate;
    private final String urlBase;

    public GestionSolicitudesClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${app.services.solicitudes.url}") String urlBase) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(15))
                .build();
        this.urlBase = quitarBarraFinal(urlBase);
    }

    public List<AsignaturaHomologadaDTO> obtenerAsignaturasHomologadas(Long idEstudiante) {
        String url = urlBase
                + "/gestionSolicitud/estudiantes/{idEstudiante}/asignaturas-homologadas";
        try {
            AsignaturaHomologadaDTO[] respuesta = restTemplate.getForObject(
                    url,
                    AsignaturaHomologadaDTO[].class,
                    idEstudiante);
            return respuesta == null ? List.of() : Arrays.asList(respuesta);
        } catch (RestClientException ex) {
            LOGGER.warn(
                    "No fue posible consultar las asignaturas homologadas del estudiante {}. "
                            + "La historia académica continuará sin esta información: {}",
                    idEstudiante,
                    ex.getMessage());
            return List.of();
        }
    }

    public List<AsignaturaCanceladaDTO> obtenerAsignaturasCanceladas(Long idEstudiante) {
        String url = urlBase
                + "/gestionSolicitud/estudiantes/{idEstudiante}/asignaturas-canceladas";
        try {
            AsignaturaCanceladaDTO[] respuesta = restTemplate.getForObject(
                    url,
                    AsignaturaCanceladaDTO[].class,
                    idEstudiante);
            return respuesta == null ? List.of() : Arrays.asList(respuesta);
        } catch (RestClientException ex) {
            LOGGER.warn(
                    "No fue posible consultar las asignaturas canceladas del estudiante {}. "
                            + "La historia académica continuará sin esta información: {}",
                    idEstudiante,
                    ex.getMessage());
            return List.of();
        }
    }

    public DocumentoFirmadoSolicitudDTO obtenerDocumentoFirmadoCancelacion(
            Long idEstudiante,
            Integer idSolicitud) {
        String url = urlBase
                + "/gestionSolicitud/estudiantes/{idEstudiante}/solicitudes/{idSolicitud}/documento-firmado";
        try {
            ResponseEntity<byte[]> respuesta = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    byte[].class,
                    idEstudiante,
                    idSolicitud);
            byte[] contenido = respuesta.getBody();
            if (contenido == null || contenido.length == 0) {
                throw new ResourceNotFoundException("La solicitud no tiene un documento final firmado.");
            }

            String nombreArchivo = respuesta.getHeaders().getContentDisposition().getFilename();
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = "solicitud-firmada.pdf";
            }
            return new DocumentoFirmadoSolicitudDTO(nombreArchivo, contenido);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("No se encontró el documento final firmado de la solicitud.");
        } catch (RestClientException ex) {
            throw new IllegalStateException(
                    "No fue posible consultar el documento final firmado en el servicio de solicitudes.",
                    ex);
        }
    }

    private static String quitarBarraFinal(String valor) {
        return valor != null && valor.endsWith("/")
                ? valor.substring(0, valor.length() - 1)
                : valor;
    }
}
