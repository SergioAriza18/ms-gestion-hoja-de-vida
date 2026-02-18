package com.maestria.gestion.hoja_de_vida.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.maestria.gestion.hoja_de_vida.domain.AsignaturaCursada;
import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.PasantiaInvestigacion;
import com.maestria.gestion.hoja_de_vida.domain.PracticaDocente;
import com.maestria.gestion.hoja_de_vida.domain.PublicacionInvestigacion;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ComplementacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.InvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDocenteDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionInvestigacionDTO;
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

    private static final DateTimeFormatter FECHA_GRADO_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        List<PasantiaInvestigacion> pasantias = pasantiaInvestigacionRepository.findByIdEstudiante(estudiante.getId());
        List<PublicacionInvestigacion> publicaciones = publicacionInvestigacionRepository
                .findByIdEstudiante(estudiante.getId());
        List<PracticaDocente> practicas = practicaDocenteRepository.findByIdEstudiante(estudiante.getId());

        List<AsignaturaCursadaDTO> fundamentacion = filtrarAsignaturasPorArea(asignaturas, AREA_FUNDAMENTACION);
        List<AsignaturaCursadaDTO> electivas = filtrarAsignaturasPorArea(asignaturas, AREA_ELECTIVAS);
        List<AsignaturaCursadaDTO> investigacionAsignaturas = filtrarAsignaturasPorArea(asignaturas, AREA_INVESTIGACION);
        List<AsignaturaCursadaDTO> competenciasEmpresariales = filtrarAsignaturasPorArea(asignaturas,
                AREA_COMPLEMENTACION);

        List<PasantiaInvestigacionDTO> pasantiasDto = pasantias.stream()
                .map(this::toPasantiaDto)
                .toList();
        List<PublicacionInvestigacionDTO> publicacionesDto = publicaciones.stream()
                .map(this::toPublicacionDto)
                .toList();
        PracticaDocenteDTO practicaDocente = practicas.stream()
                .findFirst()
                .map(this::toPracticaDto)
                .orElse(null);

        String nombreCompleto = (estudiante.getPersona().getNombre() + " " + estudiante.getPersona().getApellido()).trim();
        String fechaGrado = estudiante.getFechaGrado() == null ? null : estudiante.getFechaGrado().format(FECHA_GRADO_FORMATTER);

        InvestigacionDTO investigacion = InvestigacionDTO.builder()
                .asignaturasVistas(investigacionAsignaturas)
                .pasantias(pasantiasDto)
                .publicaciones(publicacionesDto)
                .build();

        ComplementacionDTO complementacion = ComplementacionDTO.builder()
                .practicaDocente(practicaDocente)
                .competenciasEmpresariales(competenciasEmpresariales)
                .build();

        return HistoriaAcademicaResponseDTO.builder()
                .codigoEstudiante(estudiante.getCodigo())
                .nombreCompleto(nombreCompleto)
                .correoUniversidad(estudiante.getCorreoUniversidad())
                .tituloPregrado(estudiante.getTituloPregrado())
                .fechaGrado(fechaGrado)
                .fundamentacion(fundamentacion)
                .electivas(electivas)
                .investigacion(investigacion)
                .complementacion(complementacion)
                .build();
    }

    private List<AsignaturaCursadaDTO> filtrarAsignaturasPorArea(List<AsignaturaCursada> asignaturas, String area) {
        return asignaturas.stream()
                .filter(asignatura -> area.equals(clasificarAreaPorCodigo(asignatura.getCodigoMateria())))
                .map(this::toAsignaturaDto)
                .toList();
    }

    private AsignaturaCursadaDTO toAsignaturaDto(AsignaturaCursada asignatura) {
        return AsignaturaCursadaDTO.builder()
                .periodoCursado(asignatura.getPeriodoCursado())
                .codigoMateria(asignatura.getCodigoMateria())
                .nombreMateria(asignatura.getNombreMateria())
                .creditos(asignatura.getCreditos())
                .notaDefinitiva(asignatura.getNotaDefinitiva())
                .build();
    }

    private PasantiaInvestigacionDTO toPasantiaDto(PasantiaInvestigacion pasantia) {
        return PasantiaInvestigacionDTO.builder()
                .creditosAsignados(pasantia.getCreditosAsignados())
                .acta(pasantia.getActa())
                .fechaInicio(pasantia.getFechaInicio())
                .fechaFin(pasantia.getFechaFin())
                .pais(pasantia.getPais())
                .ciudad(pasantia.getCiudad())
                .universidad(pasantia.getUniversidad())
                .build();
    }

    private PublicacionInvestigacionDTO toPublicacionDto(PublicacionInvestigacion publicacion) {
        return PublicacionInvestigacionDTO.builder()
                .creditosAsignados(publicacion.getCreditosAsignados())
                .acta(publicacion.getActa())
                .nombrePublicacion(publicacion.getNombrePublicacion())
                .tipoPublicacion(publicacion.getTipoPublicacion())
                .fechaAceptacion(publicacion.getFechaAceptacion())
                .build();
    }

    private PracticaDocenteDTO toPracticaDto(PracticaDocente practica) {
        return PracticaDocenteDTO.builder()
                .creditosAsignados(practica.getCreditosAsignados())
                .acta(practica.getActa())
                .numeroActividades(practica.getNumeroActividades())
                .build();
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
