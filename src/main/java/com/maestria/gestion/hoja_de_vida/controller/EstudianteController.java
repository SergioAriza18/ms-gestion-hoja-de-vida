package com.maestria.gestion.hoja_de_vida.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.dto.response.DistincionAcademicaDetalleDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ResolucionDistincionDTO;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes")
@RequiredArgsConstructor
@Validated
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<List<EstudianteBusquedaDTO>> listar() {
        return ResponseEntity.ok(estudianteService.listar());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<List<EstudianteBusquedaDTO>> buscar(
            @RequestParam
            @NotBlank(message = "El parámetro es obligatorio.")
            @Size(max = 100, message = "El parámetro no puede superar los 100 caracteres.")
            String valor) {
        return ResponseEntity.ok(estudianteService.buscar(valor));
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<List<EstudianteBusquedaDTO>> filtrar(
            @RequestParam(required = false) Boolean suficienciaIdiomaAprobada,
            @RequestParam(required = false)
            @Positive(message = "El semestre actual debe ser mayor que cero.")
            Integer semestreActual) {
        return ResponseEntity.ok(estudianteService.filtrar(suficienciaIdiomaAprobada, semestreActual));
    }

    @PostMapping(value = "/{codigoEstudiante}/distinciones", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<Void> registrarDistincion(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @RequestParam
            @NotNull(message = "El tipo de distinción es obligatorio.")
            TipoDistincionAcademica tipo,
            @RequestParam
            @NotBlank(message = "El número de resolución es obligatorio.")
            @Size(max = 100, message = "El número de resolución no puede superar los 100 caracteres.")
            String numeroResolucion,
            @RequestParam
            @NotNull(message = "La fecha de resolución es obligatoria.")
            @PastOrPresent(message = "La fecha de resolución no puede ser futura.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaResolucion,
            @RequestParam @NotNull(message = "La resolución en PDF es obligatoria.")
            MultipartFile resolucion) {
        estudianteService.registrarDistincion(
                codigoEstudiante,
                tipo,
                numeroResolucion,
                fechaResolucion,
                resolucion);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/{codigoEstudiante}/distinciones/{tipo}/resolucion", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("@hojaVidaAuthorization.puedeConsultar(authentication, #codigoEstudiante)")
    public ResponseEntity<byte[]> obtenerResolucionDistincion(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @PathVariable TipoDistincionAcademica tipo) {
        ResolucionDistincionDTO resolucion = estudianteService.obtenerResolucionDistincion(
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

    @GetMapping("/{codigoEstudiante}/distinciones/{tipo}")
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<DistincionAcademicaDetalleDTO> obtenerDetalleDistincion(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @PathVariable TipoDistincionAcademica tipo) {
        return ResponseEntity.ok(estudianteService.obtenerDetalleDistincion(codigoEstudiante, tipo));
    }

    @PutMapping(value = "/{codigoEstudiante}/distinciones/{tipo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<Void> editarDistincion(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @PathVariable TipoDistincionAcademica tipo,
            @RequestParam
            @NotBlank(message = "El número de resolución es obligatorio.")
            @Size(max = 100, message = "El número de resolución no puede superar los 100 caracteres.")
            String numeroResolucion,
            @RequestParam
            @NotNull(message = "La fecha de resolución es obligatoria.")
            @PastOrPresent(message = "La fecha de resolución no puede ser futura.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaResolucion,
            @RequestParam(required = false) MultipartFile resolucion) {
        estudianteService.editarDistincion(
                codigoEstudiante,
                tipo,
                numeroResolucion,
                fechaResolucion,
                resolucion);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{codigoEstudiante}/distinciones/{tipo}")
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<Void> eliminarDistincion(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @PathVariable TipoDistincionAcademica tipo) {
        estudianteService.eliminarDistincion(codigoEstudiante, tipo);
        return ResponseEntity.noContent().build();
    }
}
