package com.maestria.gestion.hoja_de_vida.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.maestria.gestion.hoja_de_vida.exception.ApiError;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import static com.maestria.gestion.hoja_de_vida.exception.ErrorCodes.BAD_REQUEST;
import static com.maestria.gestion.hoja_de_vida.exception.ErrorCodes.FORBIDDEN;
import static com.maestria.gestion.hoja_de_vida.exception.ErrorCodes.PAYLOAD_TOO_LARGE;
import static com.maestria.gestion.hoja_de_vida.exception.ErrorCodes.RESOURCE_NOT_FOUND;
import static com.maestria.gestion.hoja_de_vida.exception.ErrorCodes.UNAUTHORIZED;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";
    public static final String BAD_REQUEST_RESPONSE = "#/components/responses/BadRequest";
    public static final String UNAUTHORIZED_RESPONSE = "#/components/responses/Unauthorized";
    public static final String FORBIDDEN_RESPONSE = "#/components/responses/Forbidden";
    public static final String NOT_FOUND_RESPONSE = "#/components/responses/NotFound";
    public static final String PAYLOAD_TOO_LARGE_RESPONSE = "#/components/responses/PayloadTooLarge";

    private static final String API_ERROR_SCHEMA = "#/components/schemas/ApiError";

    @Bean
    public OpenAPI hojaVidaOpenApi() {
        Components components = new Components()
                .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                        .name(BEARER_AUTH)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addResponses("BadRequest", apiErrorResponse(
                        "La solicitud contiene parámetros, formatos o datos inválidos.",
                        400,
                        BAD_REQUEST,
                        "Debe indicar al menos un filtro.",
                        "/api/hoja-vida/estudiantes/filtrar"))
                .addResponses("Unauthorized", apiErrorResponse(
                        "No se proporcionó un JWT válido o el token expiró.",
                        401,
                        UNAUTHORIZED,
                        "Se requiere un token de autenticación válido.",
                        "/api/hoja-vida/estudiantes"))
                .addResponses("Forbidden", apiErrorResponse(
                        "El usuario autenticado no tiene permiso para ejecutar la operación.",
                        403,
                        FORBIDDEN,
                        "No tiene permisos para realizar esta operación.",
                        "/api/hoja-vida/estudiantes"))
                .addResponses("NotFound", apiErrorResponse(
                        "No existe el estudiante o el registro solicitado.",
                        404,
                        RESOURCE_NOT_FOUND,
                        "No se encontró el estudiante solicitado.",
                        "/api/hoja-vida/estudiantes/IS20260157/historia-academica"))
                .addResponses("PayloadTooLarge", apiErrorResponse(
                        "El archivo supera el tamaño máximo configurado.",
                        413,
                        PAYLOAD_TOO_LARGE,
                        "El archivo supera el tamaño máximo permitido.",
                        "/api/hoja-vida/estudiantes/IS20260157/distinciones"));

        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Hoja de Vida")
                        .version("v1")
                        .description("API del módulo de hoja de vida de la Maestría en Computación. "
                                + "Permite consultar trayectorias académicas y administrar distinciones. "
                                + "Todas las operaciones funcionales requieren un JWT emitido por el "
                                + "microservicio de autenticación."))
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    @Bean
    public OpenApiCustomiser respuestasSeguridadOpenApiCustomiser() {
        return openApi -> {
            ModelConverters.getInstance()
                    .read(ApiError.class)
                    .forEach(openApi.getComponents()::addSchemas);
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation -> {
                        operation.getResponses().addApiResponse(
                                "401",
                                new ApiResponse().$ref(UNAUTHORIZED_RESPONSE));
                        operation.getResponses().addApiResponse(
                                "403",
                                new ApiResponse().$ref(FORBIDDEN_RESPONSE));
                    }));
        };
    }

    private ApiResponse apiErrorResponse(
            String description,
            int status,
            String code,
            String message,
            String path) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(
                        MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema<>().$ref(API_ERROR_SCHEMA))
                                .addExamples("error", new Example().value(
                                        apiErrorExample(status, code, message, path)))));
    }

    private Map<String, Object> apiErrorExample(
            int status,
            String code,
            String message,
            String path) {
        Map<String, Object> example = new LinkedHashMap<>();
        example.put("fechaHora", "2026-09-05T10:15:30-05:00");
        example.put("estado", status);
        example.put("codigo", code);
        example.put("mensaje", message);
        example.put("url", path);
        return example;
    }
}
