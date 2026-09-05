package com.maestria.gestion.hoja_de_vida.service;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.dto.response.DistincionAcademicaDetalleDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ResolucionDistincionDTO;

public interface DistincionAcademicaService {

    void registrar(
            String codigoEstudiante,
            TipoDistincionAcademica tipo,
            String numeroResolucion,
            LocalDate fechaResolucion,
            MultipartFile resolucion);

    void editar(
            String codigoEstudiante,
            TipoDistincionAcademica tipo,
            String numeroResolucion,
            LocalDate fechaResolucion,
            MultipartFile resolucion);

    void eliminar(String codigoEstudiante, TipoDistincionAcademica tipo);

    DistincionAcademicaDetalleDTO obtenerDetalle(
            String codigoEstudiante,
            TipoDistincionAcademica tipo);

    ResolucionDistincionDTO obtenerResolucion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo);
}
