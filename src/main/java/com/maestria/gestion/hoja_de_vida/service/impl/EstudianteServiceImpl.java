package com.maestria.gestion.hoja_de_vida.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteResponseDTO;
import com.maestria.gestion.hoja_de_vida.mapper.EstudianteMapper;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;

    @Override
    public List<EstudianteResponseDTO> listar() {
        return estudianteRepository.findAll()
                .stream()
                .map(EstudianteMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<EstudianteResponseDTO> buscar(String valor) {
        String criterio = valor.trim();

        if (criterio.isEmpty()) {
            return List.of();
        }

        var porCodigo = estudianteRepository.findByCodigo(criterio);
        if (porCodigo.isPresent()) {
            return List.of(EstudianteMapper.toResponseDTO(porCodigo.get()));
        }

        var porIdentificacion = estudianteRepository.findByPersonaIdentificacion(criterio);
        if (porIdentificacion.isPresent()) {
            return List.of(EstudianteMapper.toResponseDTO(porIdentificacion.get()));
        }

        return estudianteRepository
                .findByPersonaNombreStartingWithIgnoreCase(criterio)
                .stream()
                .map(EstudianteMapper::toResponseDTO)
                .toList();
    }
}
