package com.maestria.gestion.hoja_de_vida.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import static com.maestria.gestion.hoja_de_vida.config.OpenApiConfig.BAD_REQUEST_RESPONSE;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Estudiantes", description = "Búsqueda y filtrado de estudiantes para coordinación.")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    @PreAuthorize("hasRole('COORDINADOR')")
    @Operation(
            summary = "Listar estudiantes",
            description = "Retorna todos los estudiantes ordenados por periodo de ingreso descendente. "
                    + "Si no existen registros, retorna una lista vacía. Requiere el rol COORDINADOR.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<EstudianteBusquedaDTO>> listar() {
        return ResponseEntity.ok(estudianteService.listar());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('COORDINADOR')")
    @Operation(
            summary = "Buscar estudiantes",
            description = "Busca primero una coincidencia exacta por código académico; si el valor es numérico, "
                    + "busca por identificación; de lo contrario, busca nombres que comiencen por el texto "
                    + "sin distinguir mayúsculas. Requiere el rol COORDINADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coincidencias encontradas; puede ser una lista vacía."),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE)
    })
    public ResponseEntity<List<EstudianteBusquedaDTO>> buscar(
            @RequestParam
            @NotBlank(message = "El parámetro es obligatorio.")
            @Size(max = 100, message = "El parámetro no puede superar los 100 caracteres.")
            @Parameter(
                    description = "Código académico, identificación exacta o inicio del nombre.",
                    example = "IS20260157")
            String valor) {
        return ResponseEntity.ok(estudianteService.buscar(valor));
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('COORDINADOR')")
    @Operation(
            summary = "Filtrar estudiantes",
            description = "Filtra por aprobación de la suficiencia de idioma, semestre actual o ambos criterios. "
                    + "Debe enviarse al menos un filtro. Requiere el rol COORDINADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estudiantes que cumplen los filtros; puede ser una lista vacía."),
            @ApiResponse(responseCode = "400", ref = BAD_REQUEST_RESPONSE)
    })
    public ResponseEntity<List<EstudianteBusquedaDTO>> filtrar(
            @Parameter(
                    description = "Indica si la suficiencia de idioma debe estar aprobada.",
                    example = "true")
            @RequestParam(required = false) Boolean suficienciaIdiomaAprobada,
            @RequestParam(required = false)
            @Positive(message = "El semestre actual debe ser mayor que cero.")
            @Parameter(description = "Semestre académico actual del estudiante.", example = "4")
            Integer semestreActual) {
        return ResponseEntity.ok(estudianteService.filtrar(suficienciaIdiomaAprobada, semestreActual));
    }
}
