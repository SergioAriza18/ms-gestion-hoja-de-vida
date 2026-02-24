package com.maestria.gestion.hoja_de_vida.service;

import java.util.List;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;

public interface EstudianteService {

    List<EstudianteBusquedaDTO> listar();

    List<EstudianteBusquedaDTO> buscar(String valor);
}
