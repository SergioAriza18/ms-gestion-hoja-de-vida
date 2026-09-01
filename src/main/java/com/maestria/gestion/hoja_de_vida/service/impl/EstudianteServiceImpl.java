package com.maestria.gestion.hoja_de_vida.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.mapper.EstudianteBusquedaMapper;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

import lombok.RequiredArgsConstructor;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.CODIGO_SUFICIENCIA_IDIOMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_APROBATORIA;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstudianteServiceImpl implements EstudianteService {

    private static final Sort ORDEN_PERIODO_INGRESO_DESC = Sort.by(Sort.Direction.DESC, "periodoIngreso");

    private final EstudianteRepository estudianteRepository;

    @Override
    public List<EstudianteBusquedaDTO> listar() {
        return estudianteRepository.findAll(ORDEN_PERIODO_INGRESO_DESC)
                .stream()
                .map(EstudianteBusquedaMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<EstudianteBusquedaDTO> buscar(String valor) {
        String criterio = valor.trim();

        if (criterio.isEmpty()) {
            return List.of();
        }

        var porCodigo = estudianteRepository.findByCodigo(criterio);
        if (porCodigo.isPresent()) {
            return List.of(EstudianteBusquedaMapper.toResponseDTO(porCodigo.get()));
        }

        Long identificacion = parseIdentificacion(criterio);
        if (identificacion != null) {
            var porIdentificacion = estudianteRepository.findByPersonaIdentificacion(identificacion);
            if (porIdentificacion.isPresent()) {
                return List.of(EstudianteBusquedaMapper.toResponseDTO(porIdentificacion.get()));
            }
        }

        return estudianteRepository
                .findAllByPersonaNombreStartingWithIgnoreCase(criterio, ORDEN_PERIODO_INGRESO_DESC)
                .stream()
                .map(EstudianteBusquedaMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<EstudianteBusquedaDTO> filtrar(Boolean suficienciaIdiomaAprobada, Integer semestreActual) {
        if (suficienciaIdiomaAprobada == null && semestreActual == null) {
            throw new IllegalArgumentException("Debe indicar al menos un filtro.");
        }

        if (suficienciaIdiomaAprobada == null) {
            return estudianteRepository
                    .findAllBySemestreAcademico(semestreActual, ORDEN_PERIODO_INGRESO_DESC)
                    .stream()
                    .map(EstudianteBusquedaMapper::toResponseDTO)
                    .toList();
        }

        return estudianteRepository.findAllBySuficienciaIdioma(
                suficienciaIdiomaAprobada,
                semestreActual,
                CODIGO_SUFICIENCIA_IDIOMA,
                NOTA_APROBATORIA)
                .stream()
                .map(EstudianteBusquedaMapper::toResponseDTO)
                .toList();
    }

    private Long parseIdentificacion(String criterio) {
        if (!criterio.chars().allMatch(Character::isDigit)) {
            return null;
        }

        try {
            return Long.parseLong(criterio);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
