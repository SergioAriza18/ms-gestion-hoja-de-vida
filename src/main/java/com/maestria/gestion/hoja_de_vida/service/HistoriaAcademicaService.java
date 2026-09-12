package com.maestria.gestion.hoja_de_vida.service;

import java.math.BigDecimal;

import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.DocumentoFirmadoSolicitudDTO;

public interface HistoriaAcademicaService {

    HistoriaAcademicaResponseDTO obtenerHistoriaAcademica(String codigoEstudiante);

    DocumentoFirmadoSolicitudDTO obtenerDocumentoFirmadoCancelacion(
            String codigoEstudiante,
            Integer idSolicitud);

    BigDecimal consultarPromedioCarrera(Long idEstudiante);
}
