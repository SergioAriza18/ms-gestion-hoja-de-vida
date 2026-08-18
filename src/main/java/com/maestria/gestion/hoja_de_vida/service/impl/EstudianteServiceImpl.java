package com.maestria.gestion.hoja_de_vida.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.maestria.gestion.hoja_de_vida.domain.DistincionAcademica;
import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.EstudianteDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;
import com.maestria.gestion.hoja_de_vida.mapper.EstudianteBusquedaMapper;
import com.maestria.gestion.hoja_de_vida.repository.DistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteDistincionAcademicaRepository;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;
import com.maestria.gestion.hoja_de_vida.service.HistoriaAcademicaService;

import lombok.RequiredArgsConstructor;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.CODIGO_SUFICIENCIA_IDIOMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_APROBATORIA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.PROMEDIO_MINIMO_EXCELENCIA;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstudianteServiceImpl implements EstudianteService {

    private static final Sort ORDEN_PERIODO_INGRESO_DESC = Sort.by(Sort.Direction.DESC, "periodoIngreso");
    private static final long TAMANO_MAXIMO_PDF = 5L * 1024 * 1024;

    private final EstudianteRepository estudianteRepository;
    private final DistincionAcademicaRepository distincionAcademicaRepository;
    private final EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;
    private final HistoriaAcademicaService historiaAcademicaService;

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

    @Override
    @Transactional
    public void registrarDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo,
            String numeroResolucion,
            LocalDate fechaResolucion,
            MultipartFile resolucion) {
        Estudiante estudiante = estudianteRepository.findByCodigo(codigoEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el estudiante solicitado."));
        DistincionAcademica distincion = distincionAcademicaRepository.findByCodigo(tipo.name())
                .orElseThrow(() -> new IllegalStateException("La distinción académica no está configurada."));

        if (estudianteDistincionAcademicaRepository
                .existsByEstudianteIdAndDistincionId(estudiante.getId(), distincion.getId())) {
            throw new IllegalArgumentException("El estudiante ya tiene registrada esta distinción académica.");
        }

        validarElegibilidadExcelencia(codigoEstudiante, tipo);
        byte[] archivoResolucion = validarYLeerPdf(resolucion);

        estudianteDistincionAcademicaRepository.save(EstudianteDistincionAcademica.builder()
                .estudiante(estudiante)
                .distincion(distincion)
                .numeroResolucion(numeroResolucion.trim())
                .fechaResolucion(fechaResolucion)
                .resolucionPdf(archivoResolucion)
                .build());
    }

    private void validarElegibilidadExcelencia(String codigoEstudiante, TipoDistincionAcademica tipo) {
        if (tipo != TipoDistincionAcademica.EXCELENCIA_ACADEMICA) {
            return;
        }

        BigDecimal promedio = historiaAcademicaService.obtenerHistoriaAcademica(codigoEstudiante)
                .getEstudiante()
                .getPromedioCarrera();
        if (promedio == null || promedio.compareTo(PROMEDIO_MINIMO_EXCELENCIA) < 0) {
            throw new IllegalArgumentException(
                    "El estudiante no cumple el promedio mínimo de 4.8 para la distinción de excelencia académica.");
        }
    }

    private byte[] validarYLeerPdf(MultipartFile resolucion) {
        if (resolucion.isEmpty()) {
            throw new IllegalArgumentException("La resolución en PDF es obligatoria.");
        }
        if (resolucion.getSize() > TAMANO_MAXIMO_PDF) {
            throw new IllegalArgumentException("La resolución en PDF no puede superar los 5 MB.");
        }
        if (!"application/pdf".equalsIgnoreCase(resolucion.getContentType())) {
            throw new IllegalArgumentException("El archivo de resolución debe ser un PDF.");
        }

        try {
            byte[] contenido = resolucion.getBytes();
            if (contenido.length < 5
                    || contenido[0] != '%'
                    || contenido[1] != 'P'
                    || contenido[2] != 'D'
                    || contenido[3] != 'F'
                    || contenido[4] != '-') {
                throw new IllegalArgumentException("El archivo de resolución debe ser un PDF válido.");
            }
            return contenido;
        } catch (IOException ex) {
            throw new IllegalArgumentException("No fue posible leer la resolución en PDF.", ex);
        }
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
