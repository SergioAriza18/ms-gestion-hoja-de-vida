package com.maestria.gestion.hoja_de_vida.service.impl;

import lombok.RequiredArgsConstructor;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ComplementacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.InvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDocenteDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.mapper.EstudianteMapper;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;
import com.maestria.gestion.hoja_de_vida.service.EstudianteService;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private static final String AREA_FUNDAMENTACION = "FUNDAMENTACION";
    private static final String AREA_ELECTIVAS = "ELECTIVAS";
    private static final String AREA_INVESTIGACION = "INVESTIGACION";
    private static final String AREA_COMPLEMENTACION = "COMPLEMENTACION";

    private static final List<AsignaturaCursadaDTO> ASIGNATURAS_MOCK = List.of(
            AsignaturaCursadaDTO.builder()
                    .periodoCursado("2024-1")
                    .codigoMateria("MOCK-FUN-01")
                    .nombreMateria("Fundamentos de Ingenieria de Software")
                    .creditos(3)
                    .notaDefinitiva("4.6")
                    .build(),
            AsignaturaCursadaDTO.builder()
                    .periodoCursado("2024-1")
                    .codigoMateria("MOCK-ELE-01")
                    .nombreMateria("Mineria de Datos Aplicada")
                    .creditos(2)
                    .notaDefinitiva("4.8")
                    .build(),
            AsignaturaCursadaDTO.builder()
                    .periodoCursado("2024-2")
                    .codigoMateria("MOCK-INV-01")
                    .nombreMateria("Seminario de Investigacion")
                    .creditos(3)
                    .notaDefinitiva("A")
                    .build(),
            AsignaturaCursadaDTO.builder()
                    .periodoCursado("2024-2")
                    .codigoMateria("MOCK-COM-01")
                    .nombreMateria("Competencias Empresariales")
                    .creditos(1)
                    .notaDefinitiva("4.7")
                    .build());

    private static final Map<String, String> AREA_POR_NOMBRE = Map.of(
            normalizarTexto("Fundamentos de Ingenieria de Software"), AREA_FUNDAMENTACION,
            normalizarTexto("Mineria de Datos Aplicada"), AREA_ELECTIVAS,
            normalizarTexto("Seminario de Investigacion"), AREA_INVESTIGACION,
            normalizarTexto("Competencias Empresariales"), AREA_COMPLEMENTACION);

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

    @Override
    public List<AsignaturaCursadaDTO> obtenerAsignaturasPorArea(String codigoEstudiante, String area) {
        validarEstudianteExiste(codigoEstudiante);
        String areaUpper = area == null ? "" : area.trim().toUpperCase(Locale.ROOT);

        return ASIGNATURAS_MOCK.stream()
                .filter(asignatura -> areaUpper.equals(clasificarAreaPorNombre(asignatura.getNombreMateria())))
                .toList();
    }

    @Override
    public List<PasantiaInvestigacionDTO> obtenerPasantias(String codigoEstudiante) {
        validarEstudianteExiste(codigoEstudiante);
        return List.of(
                PasantiaInvestigacionDTO.builder()
                        .creditosAsignados(4)
                        .acta("ACT-INV-2024-01")
                        .fechaInicio(LocalDate.of(2024, 2, 1))
                        .fechaFin(LocalDate.of(2024, 5, 31))
                        .pais("Colombia")
                        .ciudad("Popayan")
                        .universidad("Universidad del Cauca")
                        .build());
    }

    @Override
    public List<PublicacionInvestigacionDTO> obtenerPublicaciones(String codigoEstudiante) {
        validarEstudianteExiste(codigoEstudiante);
        return List.of(
                PublicacionInvestigacionDTO.builder()
                        .creditosAsignados(2)
                        .acta("ACT-PUB-2024-02")
                        .nombrePublicacion("Modelo de clasificacion academica")
                        .tipoPublicacion("Articulo")
                        .fechaAceptacion(LocalDate.of(2024, 11, 10))
                        .build());
    }

    @Override
    public List<PracticaDocenteDTO> obtenerPracticaDocente(String codigoEstudiante) {
        validarEstudianteExiste(codigoEstudiante);
        return List.of(
                PracticaDocenteDTO.builder()
                        .creditosAsignados(2)
                        .acta("ACT-PRD-2024-03")
                        .numeroActividades(6)
                        .build());
    }

    @Override
    public HistoriaAcademicaResponseDTO obtenerHistoriaAcademica(String codigoEstudiante) {
        validarEstudianteExiste(codigoEstudiante);

        List<AsignaturaCursadaDTO> fundamentacion = obtenerAsignaturasPorArea(codigoEstudiante, AREA_FUNDAMENTACION);
        List<AsignaturaCursadaDTO> electivas = obtenerAsignaturasPorArea(codigoEstudiante, AREA_ELECTIVAS);
        List<AsignaturaCursadaDTO> investigacionAsignaturas = obtenerAsignaturasPorArea(codigoEstudiante, AREA_INVESTIGACION);
        List<AsignaturaCursadaDTO> competenciasEmpresariales = obtenerAsignaturasPorArea(codigoEstudiante, AREA_COMPLEMENTACION);

        List<PasantiaInvestigacionDTO> pasantias = obtenerPasantias(codigoEstudiante);
        List<PublicacionInvestigacionDTO> publicaciones = obtenerPublicaciones(codigoEstudiante);
        PracticaDocenteDTO practicaDocente = obtenerPracticaDocente(codigoEstudiante).stream().findFirst().orElse(null);
        
        InvestigacionDTO investigacion = InvestigacionDTO.builder()
                .asignaturasVistas(investigacionAsignaturas)
                .pasantias(pasantias)
                .publicaciones(publicaciones)
                .build();

        ComplementacionDTO complementacion = ComplementacionDTO.builder()
                .practicaDocente(practicaDocente)
                .competenciasEmpresariales(competenciasEmpresariales)
                .build();

        return HistoriaAcademicaResponseDTO.builder()
                .codigoEstudiante(codigoEstudiante)
                .fundamentacion(fundamentacion)
                .electivas(electivas)
                .investigacion(investigacion)
                .complementacion(complementacion)
                .build();
    }

    private void validarEstudianteExiste(String codigoEstudiante) {
        estudianteRepository.findByCodigo(codigoEstudiante)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
    }

    private String clasificarAreaPorNombre(String nombreMateria) {
        return AREA_POR_NOMBRE.getOrDefault(normalizarTexto(nombreMateria), "SIN_CLASIFICAR");
    }

    private static String normalizarTexto(String valor) {
        if (valor == null) {
            return "";
        }
        String sinTildes = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }
}
