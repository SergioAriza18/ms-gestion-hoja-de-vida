package com.maestria.gestion.hoja_de_vida.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

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

public class HistoriaAcademicaMapper {

    private static final DateTimeFormatter FECHA_GRADO_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private HistoriaAcademicaMapper() {
    }

    public static AsignaturaCursadaDTO toAsignaturaDto(AsignaturaCursada asignatura) {
        return AsignaturaCursadaDTO.builder()
                .periodoCursado(asignatura.getPeriodoCursado())
                .codigoMateria(asignatura.getCodigoMateria())
                .nombreMateria(asignatura.getNombreMateria())
                .creditos(asignatura.getCreditos())
                .notaDefinitiva(asignatura.getNotaDefinitiva())
                .build();
    }

    public static PasantiaInvestigacionDTO toPasantiaDto(PasantiaInvestigacion pasantia) {
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

    public static PublicacionInvestigacionDTO toPublicacionDto(PublicacionInvestigacion publicacion) {
        return PublicacionInvestigacionDTO.builder()
                .creditosAsignados(publicacion.getCreditosAsignados())
                .acta(publicacion.getActa())
                .nombrePublicacion(publicacion.getNombrePublicacion())
                .tipoPublicacion(publicacion.getTipoPublicacion())
                .fechaAceptacion(publicacion.getFechaAceptacion())
                .build();
    }

    public static PracticaDocenteDTO toPracticaDto(PracticaDocente practica) {
        return PracticaDocenteDTO.builder()
                .creditosAsignados(practica.getCreditosAsignados())
                .acta(practica.getActa())
                .numeroActividades(practica.getNumeroActividades())
                .build();
    }

    public static HistoriaAcademicaResponseDTO toHistoriaAcademicaResponse(
            Estudiante estudiante,
            List<AsignaturaCursadaDTO> fundamentacion,
            List<AsignaturaCursadaDTO> electivas,
            List<AsignaturaCursadaDTO> investigacionAsignaturas,
            List<PasantiaInvestigacionDTO> pasantias,
            List<PublicacionInvestigacionDTO> publicaciones,
            PracticaDocenteDTO practicaDocente,
            List<AsignaturaCursadaDTO> competenciasEmpresariales) {

        String nombreCompleto = (estudiante.getPersona().getNombre() + " " + estudiante.getPersona().getApellido()).trim();
        String fechaGrado = estudiante.getFechaGrado() == null ? null : estudiante.getFechaGrado().format(FECHA_GRADO_FORMATTER);

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
}
