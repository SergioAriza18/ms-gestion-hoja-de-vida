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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes/{codigoEstudiante}/distinciones")
@RequiredArgsConstructor
@Validated
public class DistincionAcademicaController {

    private final DistincionAcademicaService distincionAcademicaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<Void> registrar(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @Valid @ModelAttribute RegistrarDistincionRequestDTO request) {
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
    public ResponseEntity<byte[]> obtenerResolucion(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
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
    public ResponseEntity<DistincionAcademicaDetalleDTO> obtenerDetalle(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @PathVariable TipoDistincionAcademica tipo) {
        return ResponseEntity.ok(distincionAcademicaService.obtenerDetalle(codigoEstudiante, tipo));
    }

    @PutMapping(value = "/{tipo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<Void> editar(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @PathVariable TipoDistincionAcademica tipo,
            @Valid @ModelAttribute EditarDistincionRequestDTO request) {
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
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código tiene un formato inválido.")
            String codigoEstudiante,
            @PathVariable TipoDistincionAcademica tipo) {
        distincionAcademicaService.eliminar(codigoEstudiante, tipo);
        return ResponseEntity.noContent().build();
    }
}
