package com.maestria.gestion.hoja_de_vida.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteResponseDTO;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> listar() {
        return ResponseEntity.ok(estudianteService.listar());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EstudianteResponseDTO>> buscar(@RequestParam String valor) {
        return ResponseEntity.ok(estudianteService.buscar(valor));
    }
}
