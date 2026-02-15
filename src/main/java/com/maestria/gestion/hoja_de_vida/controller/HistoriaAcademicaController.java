package com.maestria.gestion.hoja_de_vida.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class HistoriaAcademicaController {

    private final EstudianteService estudianteService;

    @GetMapping("/{codigoEstudiante}/historia-academica")
    public ResponseEntity<HistoriaAcademicaResponseDTO> getHistoriaAcademica(@PathVariable String codigoEstudiante) {
        return ResponseEntity.ok(estudianteService.obtenerHistoriaAcademica(codigoEstudiante));
    }
}
