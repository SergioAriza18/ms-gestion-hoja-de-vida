package com.maestria.gestion.hoja_de_vida.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.maestria.gestion.hoja_de_vida.domain.AsignaturaCursada;
import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDocenteDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.mapper.HistoriaAcademicaMapper;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.repository.PasantiaInvestigacionRepository;
import com.maestria.gestion.hoja_de_vida.repository.PracticaDocenteRepository;
import com.maestria.gestion.hoja_de_vida.repository.PublicacionInvestigacionRepository;
import com.maestria.gestion.hoja_de_vida.service.HistoriaAcademicaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoriaAcademicaServiceImpl implements HistoriaAcademicaService {

    private static final String AREA_FUNDAMENTACION = "FUNDAMENTACION";
    private static final String AREA_ELECTIVAS = "ELECTIVAS";
    private static final String AREA_INVESTIGACION = "INVESTIGACION";
    private static final String AREA_COMPLEMENTACION = "COMPLEMENTACION";

    private static final Map<String, String> AREA_POR_CODIGO = Map.ofEntries(
            Map.entry("M27691", AREA_FUNDAMENTACION),
            Map.entry("M27700", AREA_FUNDAMENTACION),
            Map.entry("M27701", AREA_FUNDAMENTACION),
            Map.entry("M27706", AREA_INVESTIGACION),
            Map.entry("M27708", AREA_INVESTIGACION),
            Map.entry("M27709", AREA_INVESTIGACION),
            Map.entry("M27712", AREA_INVESTIGACION),
            Map.entry("OB-11", AREA_COMPLEMENTACION));

    private final EstudianteRepository estudianteRepository;
    private final AsignaturaCursadaRepository asignaturaCursadaRepository;
    private final PasantiaInvestigacionRepository pasantiaInvestigacionRepository;
    private final PublicacionInvestigacionRepository publicacionInvestigacionRepository;
    private final PracticaDocenteRepository practicaDocenteRepository;

    @Override
    public HistoriaAcademicaResponseDTO obtenerHistoriaAcademica(String codigoEstudiante) {
        Estudiante estudiante = obtenerEstudiantePorCodigo(codigoEstudiante);
        List<AsignaturaCursada> asignaturas = asignaturaCursadaRepository.findByIdEstudiante(estudiante.getId());

        List<AsignaturaCursadaDTO> fundamentacion = filtrarAsignaturasPorArea(asignaturas, AREA_FUNDAMENTACION);
        List<AsignaturaCursadaDTO> electivas = filtrarAsignaturasPorArea(asignaturas, AREA_ELECTIVAS);
        List<AsignaturaCursadaDTO> investigacionAsignaturas = filtrarAsignaturasPorArea(asignaturas, AREA_INVESTIGACION);
        List<AsignaturaCursadaDTO> competenciasEmpresariales = filtrarAsignaturasPorArea(asignaturas, AREA_COMPLEMENTACION);

        List<PasantiaInvestigacionDTO> pasantiasDto = pasantiaInvestigacionRepository.findByIdEstudiante(estudiante.getId())
                .stream()
                .map(HistoriaAcademicaMapper::toPasantiaDto)
                .toList();
        List<PublicacionInvestigacionDTO> publicacionesDto = publicacionInvestigacionRepository.findByIdEstudiante(estudiante.getId())
                .stream()
                .map(HistoriaAcademicaMapper::toPublicacionDto)
                .toList();
        PracticaDocenteDTO practicaDocente = practicaDocenteRepository.findByIdEstudiante(estudiante.getId())
                .stream()
                .findFirst()
                .map(HistoriaAcademicaMapper::toPracticaDto)
                .orElse(null);

        return HistoriaAcademicaMapper.toHistoriaAcademicaResponse(
                estudiante,
                fundamentacion,
                electivas,
                investigacionAsignaturas,
                pasantiasDto,
                publicacionesDto,
                practicaDocente,
                competenciasEmpresariales);
    }

    private List<AsignaturaCursadaDTO> filtrarAsignaturasPorArea(List<AsignaturaCursada> asignaturas, String area) {
        return asignaturas.stream()
                .filter(asignatura -> area.equals(clasificarAreaPorCodigo(asignatura.getCodigoMateria())))
                .map(HistoriaAcademicaMapper::toAsignaturaDto)
                .toList();
    }

    private Estudiante obtenerEstudiantePorCodigo(String codigoEstudiante) {
        return estudianteRepository.findByCodigo(codigoEstudiante)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
    }

    private String clasificarAreaPorCodigo(String codigoMateria) {
        if (codigoMateria == null) {
            return AREA_ELECTIVAS;
        }
        return AREA_POR_CODIGO.getOrDefault(codigoMateria.trim().toUpperCase(Locale.ROOT), AREA_ELECTIVAS);
    }
}
