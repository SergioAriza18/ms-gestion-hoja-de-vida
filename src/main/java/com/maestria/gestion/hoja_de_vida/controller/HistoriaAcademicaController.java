package com.maestria.gestion.hoja_de_vida.controller;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.service.HistoriaAcademicaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes")
@RequiredArgsConstructor
@Validated
public class HistoriaAcademicaController {

    private final HistoriaAcademicaService historiaAcademicaService;

    @GetMapping("/{codigoEstudiante}/historia-academica")
    public ResponseEntity<HistoriaAcademicaResponseDTO> getHistoriaAcademica(
            @PathVariable
            @Size(max = 30, message = "El parámetro no puede superar los 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El parámetro tiene un formato inválido.")
            String codigoEstudiante) {
        return ResponseEntity.ok(historiaAcademicaService.obtenerHistoriaAcademica(codigoEstudiante));
    }
}
