package com.maestria.gestion.hoja_de_vida.service;

import java.util.List;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteResponseDTO;

public interface EstudianteService {

    List<EstudianteResponseDTO> listar();

    List<EstudianteResponseDTO> buscar(String valor);
}
