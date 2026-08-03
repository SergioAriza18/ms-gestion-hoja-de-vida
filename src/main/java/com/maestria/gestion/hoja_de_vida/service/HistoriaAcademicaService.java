package com.maestria.gestion.hoja_de_vida.service;

import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;

public interface HistoriaAcademicaService {

    HistoriaAcademicaResponseDTO obtenerHistoriaAcademica(String codigoEstudiante);
}
