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

    private Long parseIdentificacion(String criterio) {
        String normalizado = criterio.trim();
        if (normalizado.isEmpty() || !normalizado.chars().allMatch(Character::isDigit)) {
            return null;
        }

        try {
            return Long.parseLong(normalizado);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
