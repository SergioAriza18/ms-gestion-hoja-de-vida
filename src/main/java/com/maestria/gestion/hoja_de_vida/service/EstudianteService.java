package com.maestria.gestion.hoja_de_vida.service;

import java.util.List;

import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDocenteDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionInvestigacionDTO;

public interface EstudianteService {

    List<EstudianteResponseDTO> listar();

    List<EstudianteResponseDTO> buscar(String valor);

    List<AsignaturaCursadaDTO> obtenerAsignaturasPorArea(String codigoEstudiante, String area);

    List<PasantiaInvestigacionDTO> obtenerPasantias(String codigoEstudiante);

    List<PublicacionInvestigacionDTO> obtenerPublicaciones(String codigoEstudiante);

    List<PracticaDocenteDTO> obtenerPracticaDocente(String codigoEstudiante);

    HistoriaAcademicaResponseDTO obtenerHistoriaAcademica(String codigoEstudiante);
}
