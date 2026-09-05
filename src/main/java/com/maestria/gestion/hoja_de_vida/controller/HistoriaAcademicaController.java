package com.maestria.gestion.hoja_de_vida.controller;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.service.HistoriaAcademicaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import static com.maestria.gestion.hoja_de_vida.config.OpenApiConfig.BAD_REQUEST_RESPONSE;
import static com.maestria.gestion.hoja_de_vida.config.OpenApiConfig.NOT_FOUND_RESPONSE;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Historia académica", description = "Consulta consolidada de la hoja de vida académica.")
public class HistoriaAcademicaController {

    private final HistoriaAcademicaService historiaAcademicaService;

    @GetMapping("/{codigoEstudiante}/historia-academica")
    @PreAuthorize("@hojaVidaAuthorization.puedeConsultar(authentication, #codigoEstudiante)")
    @Operation(
            summary = "Consultar historia académica",
            description = "Consolida datos del estudiante, asignaturas por área, créditos cumplidos, promedio, "
                    + "suficiencia de idioma, investigación, práctica docente, trabajo de grado y distinciones. "
                    + "El promedio excluye las materias especiales y se presenta con la aproximación institucional. "
                    + "Coordinación puede consultar cualquier estudiante; un estudiante únicamente su propia historia.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historia académica consolidada correctamente."),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE),
            @ApiResponse(responseCode = "404", ref = NOT_FOUND_RESPONSE)
    })
    public ResponseEntity<HistoriaAcademicaResponseDTO> getHistoriaAcademica(
            @PathVariable
            @Size(max = 30, message = "El parámetro no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El parámetro tiene un formato inválido.")
            @Parameter(description = "Código académico del estudiante.", example = "IS20260157")
            String codigoEstudiante) {
        return ResponseEntity.ok(historiaAcademicaService.obtenerHistoriaAcademica(codigoEstudiante));
    }
}
