package com.maestria.gestion.hoja_de_vida.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.maestria.gestion.hoja_de_vida.domain.DistincionAcademica;
import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.EstudianteDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;
import com.maestria.gestion.hoja_de_vida.config.ArchivoResolucionProperties;
import com.maestria.gestion.hoja_de_vida.dto.response.DistincionAcademicaDetalleDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteBusquedaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ResolucionDistincionDTO;
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
    private final EstudianteRepository estudianteRepository;
    private final DistincionAcademicaRepository distincionAcademicaRepository;
    private final EstudianteDistincionAcademicaRepository estudianteDistincionAcademicaRepository;
    private final HistoriaAcademicaService historiaAcademicaService;
    private final ArchivoResolucionProperties archivoResolucionProperties;

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
        validarTipoDistincion(tipo);
        String numeroResolucionNormalizado = validarNumeroResolucion(numeroResolucion);
        validarFechaResolucion(fechaResolucion);

        Estudiante estudiante = estudianteRepository.findByCodigo(codigoEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el estudiante solicitado."));
        DistincionAcademica distincion = distincionAcademicaRepository.findByCodigo(tipo.name())
                .orElseThrow(() -> new IllegalStateException("La distinción académica no está configurada."));

        if (estudianteDistincionAcademicaRepository
                .existsByEstudianteIdAndDistincionId(estudiante.getId(), distincion.getId())) {
            throw new IllegalArgumentException("El estudiante ya tiene registrada esta distinción académica.");
        }

        byte[] archivoResolucion = validarYLeerPdf(resolucion);
        validarElegibilidadExcelencia(estudiante.getId(), tipo);

        try {
            estudianteDistincionAcademicaRepository.saveAndFlush(EstudianteDistincionAcademica.builder()
                    .estudiante(estudiante)
                    .distincion(distincion)
                    .numeroResolucion(numeroResolucionNormalizado)
                    .fechaResolucion(fechaResolucion)
                    .resolucionPdf(archivoResolucion)
                    .build());
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "El estudiante ya tiene registrada esta distinción académica.", ex);
        }
    }

    @Override
    @Transactional
    public void editarDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo,
            String numeroResolucion,
            LocalDate fechaResolucion,
            MultipartFile resolucion) {
        validarTipoDistincion(tipo);
        String numeroResolucionNormalizado = validarNumeroResolucion(numeroResolucion);
        validarFechaResolucion(fechaResolucion);

        EstudianteDistincionAcademica estudianteDistincion = obtenerDistincionEstudiante(
                codigoEstudiante,
                tipo,
                "No se encontró la distinción académica solicitada para el estudiante.");
        estudianteDistincion.setNumeroResolucion(numeroResolucionNormalizado);
        estudianteDistincion.setFechaResolucion(fechaResolucion);

        if (resolucion != null) {
            estudianteDistincion.setResolucionPdf(validarYLeerPdf(resolucion));
        }

        estudianteDistincionAcademicaRepository.saveAndFlush(estudianteDistincion);
    }

    @Override
    @Transactional
    public void eliminarDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo) {
        validarTipoDistincion(tipo);
        estudianteDistincionAcademicaRepository.delete(obtenerDistincionEstudiante(
                codigoEstudiante,
                tipo,
                "No se encontró la distinción académica solicitada para el estudiante."));
    }

    @Override
    public DistincionAcademicaDetalleDTO obtenerDetalleDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo) {
        validarTipoDistincion(tipo);
        EstudianteDistincionAcademica estudianteDistincion = obtenerDistincionEstudiante(
                codigoEstudiante,
                tipo,
                "No se encontró la distinción académica solicitada para el estudiante.");

        return DistincionAcademicaDetalleDTO.builder()
                .tipo(tipo)
                .numeroResolucion(estudianteDistincion.getNumeroResolucion())
                .fechaResolucion(estudianteDistincion.getFechaResolucion())
                .build();
    }

    @Override
    public ResolucionDistincionDTO obtenerResolucionDistincion(
            String codigoEstudiante,
            TipoDistincionAcademica tipo) {
        validarTipoDistincion(tipo);
        EstudianteDistincionAcademica estudianteDistincion = obtenerDistincionEstudiante(
                codigoEstudiante,
                tipo,
                "No se encontró la resolución de la distinción solicitada.");

        return new ResolucionDistincionDTO(
                crearNombreArchivoResolucion(estudianteDistincion.getNumeroResolucion()),
                estudianteDistincion.getResolucionPdf());
    }

    private String crearNombreArchivoResolucion(String numeroResolucion) {
        String nombreNormalizado = Normalizer.normalize(numeroResolucion, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z0-9._-]+", "-")
                .replaceAll("^[.-]+|[.-]+$", "");

        return (nombreNormalizado.isBlank() ? "resolucion" : nombreNormalizado) + ".pdf";
    }

    private EstudianteDistincionAcademica obtenerDistincionEstudiante(
            String codigoEstudiante,
            TipoDistincionAcademica tipo,
            String mensajeNoEncontrado) {
        return estudianteDistincionAcademicaRepository
                .findByEstudianteCodigoAndDistincionCodigo(codigoEstudiante, tipo.name())
                .orElseThrow(() -> new ResourceNotFoundException(mensajeNoEncontrado));
    }

    private void validarTipoDistincion(TipoDistincionAcademica tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de distinción es obligatorio.");
        }
    }

    private String validarNumeroResolucion(String numeroResolucion) {
        if (numeroResolucion == null || numeroResolucion.isBlank()) {
            throw new IllegalArgumentException("El número de resolución es obligatorio.");
        }

        String numeroNormalizado = numeroResolucion.trim();
        if (numeroNormalizado.length() > 100) {
            throw new IllegalArgumentException("El número de resolución no puede superar los 100 caracteres.");
        }
        return numeroNormalizado;
    }

    private void validarFechaResolucion(LocalDate fechaResolucion) {
        if (fechaResolucion == null) {
            throw new IllegalArgumentException("La fecha de resolución es obligatoria.");
        }
        if (fechaResolucion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de resolución no puede ser futura.");
        }
    }

    private void validarElegibilidadExcelencia(Long idEstudiante, TipoDistincionAcademica tipo) {
        if (tipo != TipoDistincionAcademica.EXCELENCIA_ACADEMICA) {
            return;
        }

        BigDecimal promedio = historiaAcademicaService.consultarPromedioCarrera(idEstudiante);
        if (promedio == null || promedio.compareTo(PROMEDIO_MINIMO_EXCELENCIA) < 0) {
            throw new IllegalArgumentException(
                    "El estudiante no cumple el promedio mínimo de 4.8 para la distinción de excelencia académica.");
        }
    }

    private byte[] validarYLeerPdf(MultipartFile resolucion) {
        if (resolucion.isEmpty()) {
            throw new IllegalArgumentException("La resolución en PDF es obligatoria.");
        }
        if (resolucion.getSize() > archivoResolucionProperties.getTamanoMaximo().toBytes()) {
            throw new IllegalArgumentException("La resolución en PDF supera el tamaño máximo permitido.");
        }
        String tipoContenido = resolucion.getContentType();
        if (tipoContenido != null
                && !tipoContenido.isBlank()
                && !"application/pdf".equalsIgnoreCase(tipoContenido)
                && !"application/octet-stream".equalsIgnoreCase(tipoContenido)) {
            throw new IllegalArgumentException("El archivo de resolución debe ser un PDF.");
        }

        byte[] contenido;
        try {
            contenido = resolucion.getBytes();
        } catch (IOException ex) {
            throw new IllegalArgumentException("No fue posible leer la resolución en PDF.", ex);
        }

        if (!tieneFirmaPdf(contenido)) {
            throw new IllegalArgumentException("El archivo de resolución debe ser un PDF válido.");
        }

        try (PDDocument documento = Loader.loadPDF(contenido)) {
            if (documento.isEncrypted() || documento.getNumberOfPages() == 0) {
                throw new IllegalArgumentException("El archivo de resolución debe ser un PDF válido y legible.");
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("El archivo de resolución debe ser un PDF válido.", ex);
        }

        return contenido;
    }

    private boolean tieneFirmaPdf(byte[] contenido) {
        return contenido.length >= 5
                && contenido[0] == '%'
                && contenido[1] == 'P'
                && contenido[2] == 'D'
                && contenido[3] == 'F'
                && contenido[4] == '-';
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
