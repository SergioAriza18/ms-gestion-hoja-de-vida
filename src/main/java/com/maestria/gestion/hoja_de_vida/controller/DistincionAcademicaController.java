package com.maestria.gestion.hoja_de_vida.controller;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.dto.request.EditarDistincionRequestDTO;
import com.maestria.gestion.hoja_de_vida.dto.request.RegistrarDistincionRequestDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.DistincionAcademicaDetalleDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ResolucionDistincionDTO;
import com.maestria.gestion.hoja_de_vida.service.DistincionAcademicaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import static com.maestria.gestion.hoja_de_vida.config.OpenApiConfig.BAD_REQUEST_RESPONSE;
import static com.maestria.gestion.hoja_de_vida.config.OpenApiConfig.NOT_FOUND_RESPONSE;
import static com.maestria.gestion.hoja_de_vida.config.OpenApiConfig.PAYLOAD_TOO_LARGE_RESPONSE;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones")
@RequiredArgsConstructor
@Validated
@Tag(name = "Distinciones académicas", description = "Registro, consulta, edición y eliminación de distinciones.")
public class DistincionAcademicaController {

    private final DistincionAcademicaService distincionAcademicaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('COORDINADOR')")
    @Operation(
            summary = "Registrar una distinción",
            description = "Registra una distinción única por estudiante y tipo. La fecha no puede ser futura y "
                    + "la resolución debe ser un PDF válido, legible y no cifrado. Para EXCELENCIA_ACADEMICA, "
                    + "el promedio institucional mostrado debe ser igual o superior a 4.8. "
                    + "Requiere el rol COORDINADOR.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la distinción y resolución PDF obligatoria.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = RegistrarDistincionRequestDTO.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Distinción registrada correctamente."),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE),
            @ApiResponse(responseCode = "404", ref = NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "413", ref = PAYLOAD_TOO_LARGE_RESPONSE)
    })
    public ResponseEntity<Void> registrar(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            @Parameter(description = "Código académico del estudiante.", example = "IS20260157")
            String codigoEstudiante,
            @Parameter(hidden = true) @Valid @ModelAttribute RegistrarDistincionRequestDTO request) {
        distincionAcademicaService.registrar(
                codigoEstudiante,
                request.getTipo(),
                request.getNumeroResolucion(),
                request.getFechaResolucion(),
                request.getResolucion());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/{tipo}/resolucion", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("@hojaVidaAuthorization.puedeConsultar(authentication, #codigoEstudiante)")
    @Operation(
            summary = "Consultar la resolución de una distinción",
            description = "Entrega el PDF en línea y utiliza el número de resolución normalizado como nombre "
                    + "del archivo. Coordinación puede consultar cualquier estudiante; un estudiante únicamente el propio.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resolución en formato PDF.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE),
            @ApiResponse(responseCode = "404", ref = NOT_FOUND_RESPONSE)
    })
    public ResponseEntity<byte[]> obtenerResolucion(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            @Parameter(description = "Código académico del estudiante.", example = "IS20260157")
            String codigoEstudiante,
            @Parameter(
                    description = "Tipo de distinción cuya resolución se descargará.",
                    example = "EXCELENCIA_ACADEMICA")
            @PathVariable TipoDistincionAcademica tipo) {
        ResolucionDistincionDTO resolucion = distincionAcademicaService.obtenerResolucion(
                codigoEstudiante,
                tipo);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resolucion.getNombreArchivo() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, private, max-age=0")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header("X-Content-Type-Options", "nosniff")
                .contentLength(resolucion.getContenido().length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resolucion.getContenido());
    }

    @GetMapping("/{tipo}")
    @PreAuthorize("hasRole('COORDINADOR')")
    @Operation(
            summary = "Consultar una distinción",
            description = "Obtiene el tipo, número y fecha de resolución para visualizar o precargar el "
                    + "formulario de edición. No incluye el contenido del PDF. Requiere el rol COORDINADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos de la distinción encontrados."),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE),
            @ApiResponse(responseCode = "404", ref = NOT_FOUND_RESPONSE)
    })
    public ResponseEntity<DistincionAcademicaDetalleDTO> obtenerDetalle(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            @Parameter(description = "Código académico del estudiante.", example = "IS20260157")
            String codigoEstudiante,
            @Parameter(description = "Tipo de distinción registrada.", example = "EXCELENCIA_ACADEMICA")
            @PathVariable TipoDistincionAcademica tipo) {
        return ResponseEntity.ok(distincionAcademicaService.obtenerDetalle(codigoEstudiante, tipo));
    }

    @PutMapping(value = "/{tipo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('COORDINADOR')")
    @Operation(
            summary = "Editar una distinción",
            description = "Actualiza el número y la fecha de resolución. Si se adjunta un PDF, reemplaza el "
                    + "archivo existente; si se omite, conserva el actual. Requiere el rol COORDINADOR.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados; la resolución PDF es opcional.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = EditarDistincionRequestDTO.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Distinción actualizada correctamente."),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE),
            @ApiResponse(responseCode = "404", ref = NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "413", ref = PAYLOAD_TOO_LARGE_RESPONSE)
    })
    public ResponseEntity<Void> editar(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            @Parameter(description = "Código académico del estudiante.", example = "IS20260157")
            String codigoEstudiante,
            @Parameter(description = "Tipo de distinción registrada.", example = "EXCELENCIA_ACADEMICA")
            @PathVariable TipoDistincionAcademica tipo,
            @Parameter(hidden = true) @Valid @ModelAttribute EditarDistincionRequestDTO request) {
        distincionAcademicaService.editar(
                codigoEstudiante,
                tipo,
                request.getNumeroResolucion(),
                request.getFechaResolucion(),
                request.getResolucion());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tipo}")
    @PreAuthorize("hasRole('COORDINADOR')")
    @Operation(
            summary = "Eliminar una distinción",
            description = "Elimina la asociación de la distinción y su resolución PDF para el estudiante. "
                    + "Requiere el rol COORDINADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Distinción eliminada correctamente."),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE),
            @ApiResponse(responseCode = "404", ref = NOT_FOUND_RESPONSE)
    })
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            @Parameter(description = "Código académico del estudiante.", example = "IS20260157")
            String codigoEstudiante,
            @Parameter(description = "Tipo de distinción registrada.", example = "EXCELENCIA_ACADEMICA")
            @PathVariable TipoDistincionAcademica tipo) {
        distincionAcademicaService.eliminar(codigoEstudiante, tipo);
        return ResponseEntity.noContent().build();
    }
}
