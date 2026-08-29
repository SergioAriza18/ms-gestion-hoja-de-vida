package com.maestria.gestion.hoja_de_vida.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;

import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionDTO;

import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;
import com.maestria.gestion.hoja_de_vida.mapper.HistoriaAcademicaMapper;

import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteDistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.PasantiaRepository;
import com.maestria.gestion.hoja_de_vida.repository.PracticaRepository;
import com.maestria.gestion.hoja_de_vida.repository.PublicacionRepository;

import com.maestria.gestion.hoja_de_vida.service.HistoriaAcademicaService;

import lombok.RequiredArgsConstructor;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.AREA_COMPLEMENTACION;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.AREA_ELECTIVAS;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.AREA_FUNDAMENTACION;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.AREA_INVESTIGACION;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.AREA_REQUISITOS_GRADO;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_APROBATORIA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.VALOR_TEXTO_VACIO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoriaAcademicaServiceImpl implements HistoriaAcademicaService {

        private static final Set<Long> AREAS_CON_CREDITOS = Set.of(
                        AREA_FUNDAMENTACION,
                        AREA_ELECTIVAS,
                        AREA_INVESTIGACION,
                        AREA_COMPLEMENTACION);

        private final EstudianteRepository estudianteRepository;
        private final AsignaturaCursadaRepository asignaturaCursadaRepository;
        private final PasantiaRepository pasantiaInvestigacionRepository;
        private final PublicacionRepository publicacionInvestigacionRepository;
        private final PracticaRepository practicaRepository;
        private final EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;

        @Override
        public HistoriaAcademicaResponseDTO obtenerHistoriaAcademica(String codigoEstudiante) {
                Estudiante estudiante = obtenerEstudiantePorCodigo(codigoEstudiante);
                Long idEstudiante = estudiante.getId();

                List<AsignaturaCursadaResumen> asignaturas = asignaturaCursadaRepository
                                .findAsignaturasResumenByEstudianteId(idEstudiante);

                List<AsignaturaCursadaDTO> fundamentacion = filtrarAsignaturasPorArea(
                                asignaturas, AREA_FUNDAMENTACION);

                List<AsignaturaCursadaDTO> competenciasEmpresariales = filtrarAsignaturasPorArea(
                                asignaturas, AREA_COMPLEMENTACION);

                List<AsignaturaCursadaDTO> electivas = filtrarAsignaturasPorArea(
                                asignaturas, AREA_ELECTIVAS);

                List<AsignaturaCursadaDTO> investigacionAsignaturas = filtrarAsignaturasPorArea(
                                asignaturas, AREA_INVESTIGACION);

                List<AsignaturaCursadaDTO> requisitosGrado = filtrarAsignaturasPorArea(
                                asignaturas, AREA_REQUISITOS_GRADO);

                List<PasantiaDTO> pasantiasDto = pasantiaInvestigacionRepository
                                .findAllByIdEstudiante(idEstudiante)
                                .stream()
                                .map(HistoriaAcademicaMapper::toPasantiaDto)
                                .toList();
                List<PublicacionDTO> publicacionesDto = publicacionInvestigacionRepository
                                .findAllByIdEstudiante(idEstudiante)
                                .stream()
                                .map(HistoriaAcademicaMapper::toPublicacionDto)
                                .toList();
                List<PracticaDTO> practicasDocentes = practicaRepository
                                .findAllByIdEstudiante(idEstudiante)
                                .stream()
                                .map(HistoriaAcademicaMapper::toPracticaDto)
                                .toList();
                Integer creditosCumplidos = calcularCreditosCumplidos(
                                asignaturas,
                                pasantiasDto,
                                publicacionesDto,
                                practicasDocentes);
                BigDecimal promedioCarrera = calcularPromedioCarrera(asignaturas);
                String tituloTesis = estudianteRepository
                                .findTituloTesisByEstudianteId(idEstudiante)
                                .orElse(VALOR_TEXTO_VACIO);
                var directorCodirector = estudianteRepository
                                .findDirectorCodirectorByEstudianteId(idEstudiante);
                String directorTesis = directorCodirector
                                .map(EstudianteRepository.DirectorCodirectorResumen::getDirector)
                                .orElse(VALOR_TEXTO_VACIO);
                String codirectorTesis = directorCodirector
                                .map(EstudianteRepository.DirectorCodirectorResumen::getCodirector)
                                .orElse(VALOR_TEXTO_VACIO);
                List<String> distincionesAcademicas = estudianteDistincionAcademicaRepository
                                .findCodigosByEstudianteId(estudiante.getId());
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
                                promedioCarrera,
                                tituloTesis,
                                directorTesis,
                                codirectorTesis,
                                requisitosGrado,
                                distincionesAcademicas);
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
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "No se encontró el estudiante solicitado."));
        }

        private Integer calcularCreditosCumplidos(
                        List<AsignaturaCursadaResumen> asignaturas,
                        List<PasantiaDTO> pasantias,
                        List<PublicacionDTO> publicaciones,
                        List<PracticaDTO> practicasDocentes) {

                int creditosAsignaturas = asignaturas.stream()
                                .filter(asignatura -> AREAS_CON_CREDITOS.contains(asignatura.getAreaFormacion()))
                                .filter(asignatura -> asignatura.getNota() != null
                                                && asignatura.getNota().compareTo(NOTA_APROBATORIA) >= 0)
                                .map(AsignaturaCursadaResumen::getCreditos)
                                .filter(credito -> credito != null && credito > 0)
                                .mapToInt(Integer::intValue)
                                .sum();

                int creditosPasantias = pasantias.stream()
                                .map(PasantiaDTO::getCreditosAsignados)
                                .filter(credito -> credito != null && credito > 0)
                                .mapToInt(Integer::intValue)
                                .sum();

                int creditosPublicaciones = publicaciones.stream()
                                .map(PublicacionDTO::getCreditosAsignados)
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

        private BigDecimal calcularPromedioCarrera(List<AsignaturaCursadaResumen> asignaturas) {
                BigDecimal sumaNotas = BigDecimal.ZERO;
                int totalNotas = 0;

                for (AsignaturaCursadaResumen asignatura : asignaturas) {
                        BigDecimal nota = asignatura.getNota();

                        if (nota == null) {
                                continue;
                        }

                        sumaNotas = sumaNotas.add(nota);
                        totalNotas++;
                }

                if (totalNotas == 0) {
                        return null;
                }

                return sumaNotas.divide(BigDecimal.valueOf(totalNotas), 2, RoundingMode.HALF_UP);
        }

}
