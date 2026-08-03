package com.maestria.gestion.hoja_de_vida.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hoja-vida/estudiantes")
@RequiredArgsConstructor
@Validated
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<EstudianteBusquedaDTO>> listar() {
        return ResponseEntity.ok(estudianteService.listar());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EstudianteBusquedaDTO>> buscar(
            @RequestParam
            @NotBlank(message = "El parámetro es obligatorio.")
            @Size(max = 100, message = "El parámetro no puede superar los 100 caracteres.")
            String valor) {
        return ResponseEntity.ok(estudianteService.buscar(valor));
    }
}
