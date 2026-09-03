package com.maestria.gestion.hoja_de_vida.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.dto.response.DistincionAcademicaDetalleDTO;
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

    void editarDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo,
            String numeroResolucion,
            LocalDate fechaResolucion,
            MultipartFile resolucion);

    void eliminarDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo);

    DistincionAcademicaDetalleDTO obtenerDetalleDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo);

    byte[] obtenerResolucionDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo);
}
