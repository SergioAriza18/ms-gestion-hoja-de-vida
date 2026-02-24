package com.maestria.gestion.hoja_de_vida.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.mapper.EstudianteBusquedaMapper;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;

    @Override
    public List<EstudianteBusquedaDTO> listar() {
        return estudianteRepository.findAll()
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

        var porIdentificacion = estudianteRepository.findByPersonaIdentificacion(criterio);
        if (porIdentificacion.isPresent()) {
            return List.of(EstudianteBusquedaMapper.toResponseDTO(porIdentificacion.get()));
        }

        return estudianteRepository
                .findByPersonaNombreStartingWithIgnoreCase(criterio)
                .stream()
                .map(EstudianteBusquedaMapper::toResponseDTO)
                .toList();
    }
}
