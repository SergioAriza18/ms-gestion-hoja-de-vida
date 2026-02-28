package com.maestria.gestion.hoja_de_vida.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;

import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionInvestigacionDTO;

import com.maestria.gestion.hoja_de_vida.mapper.HistoriaAcademicaMapper;

import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.repository.PasantiaInvestigacionRepository;
import com.maestria.gestion.hoja_de_vida.repository.PracticaRepository;
import com.maestria.gestion.hoja_de_vida.repository.PublicacionInvestigacionRepository;

import com.maestria.gestion.hoja_de_vida.service.HistoriaAcademicaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoriaAcademicaServiceImpl implements HistoriaAcademicaService {

        private static final Long AREA_FUNDAMENTACION = 5L;
        private static final Long AREA_ELECTIVAS = 6L;
        private static final Long AREA_INVESTIGACION = 7L;
        private static final Long AREA_COMPLEMENTACION = 8L;
        private static final Long AREA_REQUISITOS_GRADO = 9L;
        private static final String VALOR_TEXTO_VACIO = "";

        private final EstudianteRepository estudianteRepository;
        private final AsignaturaCursadaRepository asignaturaCursadaRepository;
        private final PasantiaInvestigacionRepository pasantiaInvestigacionRepository;
        private final PublicacionInvestigacionRepository publicacionInvestigacionRepository;
        private final PracticaRepository PracticaRepository;

        @Override
        public HistoriaAcademicaResponseDTO obtenerHistoriaAcademica(String codigoEstudiante) {
                Estudiante estudiante = obtenerEstudiantePorCodigo(codigoEstudiante);
                List<AsignaturaCursadaResumen> asignaturas = asignaturaCursadaRepository
                                .findAsignaturasResumenByEstudianteId(estudiante.getId());

                List<AsignaturaCursadaDTO> fundamentacion = filtrarAsignaturasPorArea(asignaturas, AREA_FUNDAMENTACION);
                List<AsignaturaCursadaDTO> competenciasEmpresariales = filtrarAsignaturasPorArea(asignaturas,
                                AREA_COMPLEMENTACION);
                List<AsignaturaCursadaDTO> electivas = filtrarAsignaturasPorArea(asignaturas, AREA_ELECTIVAS);
                List<AsignaturaCursadaDTO> investigacionAsignaturas = filtrarAsignaturasPorArea(asignaturas, AREA_INVESTIGACION);
                List<AsignaturaCursadaDTO> requisitosGrado = filtrarAsignaturasPorArea(asignaturas,
                                AREA_REQUISITOS_GRADO);

                List<PasantiaInvestigacionDTO> pasantiasDto = pasantiaInvestigacionRepository
                                .findAllByIdEstudiante(estudiante.getId())
                                .stream()
                                .map(HistoriaAcademicaMapper::toPasantiaDto)
                                .toList();
                List<PublicacionInvestigacionDTO> publicacionesDto = publicacionInvestigacionRepository
                                .findAllByIdEstudiante(estudiante.getId())
                                .stream()
                                .map(HistoriaAcademicaMapper::toPublicacionDto)
                                .toList();
                List<PracticaDTO> practicasDocentes = PracticaRepository
                                .findAllByIdEstudiante(estudiante.getId())
                                .stream()
                                .map(HistoriaAcademicaMapper::toPracticaDto)
                                .toList();
                Integer creditosCumplidos = calcularCreditosCumplidos(
                                fundamentacion,
                                electivas,
                                investigacionAsignaturas,
                                competenciasEmpresariales,
                                pasantiasDto,
                                publicacionesDto,
                                practicasDocentes);
                String tituloTesis = estudianteRepository
                                .findTituloTesisByEstudianteId(estudiante.getId())
                                .orElse(VALOR_TEXTO_VACIO);
                EstudianteRepository.DirectorCodirectorResumen directorCodirector = estudianteRepository
                                .findDirectorCodirectorByEstudianteId(estudiante.getId())
                                .orElse(null);
                String directorTesis = directorCodirector == null || directorCodirector.getDirector() == null
                                ? VALOR_TEXTO_VACIO
                                : directorCodirector.getDirector();
                String codirectorTesis = directorCodirector == null || directorCodirector.getCodirector() == null
                                ? VALOR_TEXTO_VACIO
                                : directorCodirector.getCodirector();

                return HistoriaAcademicaMapper.toHistoriaAcademicaResponse(
                                estudiante,
                                fundamentacion,
                                electivas,
                                investigacionAsignaturas,
                                pasantiasDto,
                                publicacionesDto,
                                practicasDocentes,
                                competenciasEmpresariales,
                                creditosCumplidos,
                                tituloTesis,
                                directorTesis,
                                codirectorTesis,
                                requisitosGrado);
        }

        private List<AsignaturaCursadaDTO> filtrarAsignaturasPorArea(List<AsignaturaCursadaResumen> asignaturas,
                        Long area) {
                return asignaturas.stream()
                                .filter(asignatura -> area.equals(asignatura.getAreaFormacion()))
                                .map(HistoriaAcademicaMapper::toAsignaturaDto)
                                .toList();
        }

        private Estudiante obtenerEstudiantePorCodigo(String codigoEstudiante) {
                return estudianteRepository.findByCodigo(codigoEstudiante)
                                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        }

        private Integer calcularCreditosCumplidos(
                        List<AsignaturaCursadaDTO> fundamentacion,
                        List<AsignaturaCursadaDTO> electivas,
                        List<AsignaturaCursadaDTO> investigacionAsignaturas,
                        List<AsignaturaCursadaDTO> competenciasEmpresariales,
                        List<PasantiaInvestigacionDTO> pasantias,
                        List<PublicacionInvestigacionDTO> publicaciones,
                        List<PracticaDTO> practicasDocentes) {

                int creditosAsignaturas = sumarCreditosAsignaturasAprobadas(fundamentacion)
                                + sumarCreditosAsignaturasAprobadas(electivas)
                                + sumarCreditosAsignaturasAprobadas(investigacionAsignaturas)
                                + sumarCreditosAsignaturasAprobadas(competenciasEmpresariales);

                int creditosPasantias = pasantias.stream()
                                .map(PasantiaInvestigacionDTO::getCreditosAsignados)
                                .filter(credito -> credito != null && credito > 0)
                                .mapToInt(Integer::intValue)
                                .sum();

                int creditosPublicaciones = publicaciones.stream()
                                .map(PublicacionInvestigacionDTO::getCreditosAsignados)
                                .filter(credito -> credito != null && credito > 0)
                                .mapToInt(Integer::intValue)
                                .sum();

                int creditosPracticas = practicasDocentes.stream()
                                .map(PracticaDTO::getCreditosAsignados)
                                .filter(credito -> credito != null && credito > 0)
                                .mapToInt(Integer::intValue)
                                .sum();

                return creditosAsignaturas + creditosPasantias + creditosPublicaciones + creditosPracticas;
        }

        private int sumarCreditosAsignaturasAprobadas(List<AsignaturaCursadaDTO> asignaturas) {
                return asignaturas.stream()
                                .filter(this::asignaturaAprobada)
                                .map(AsignaturaCursadaDTO::getCreditos)
                                .filter(credito -> credito != null && credito > 0)
                                .mapToInt(Integer::intValue)
                                .sum();
        }

        private boolean asignaturaAprobada(AsignaturaCursadaDTO asignatura) {
                String notaDefinitiva = asignatura.getNotaDefinitiva();
                if (notaDefinitiva == null || notaDefinitiva.isBlank()) {
                        return false;
                }

                String notaNormalizada = notaDefinitiva.trim().toUpperCase(Locale.ROOT);
                if ("A".equals(notaNormalizada)) {
                        return true;
                }

                try {
                        return Double.parseDouble(notaDefinitiva.trim().replace(',', '.')) >= 3.5d;
                } catch (NumberFormatException ex) {
                        return false;
                }
        }
}
