package com.maestria.gestion.hoja_de_vida.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;

public interface EstudianteService {

    List<EstudianteBusquedaDTO> listar();

    List<EstudianteBusquedaDTO> buscar(String valor);

    List<EstudianteBusquedaDTO> filtrar(Boolean suficienciaIdiomaAprobada, Integer semestreActual);

    void registrarDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo,
            String numeroResolucion,
            LocalDate fechaResolucion,
            MultipartFile resolucion);
}
