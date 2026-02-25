package com.maestria.gestion.hoja_de_vida.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.PasantiaInvestigacion;
import com.maestria.gestion.hoja_de_vida.domain.PracticaDocente;
import com.maestria.gestion.hoja_de_vida.domain.PublicacionInvestigacion;
import com.maestria.gestion.hoja_de_vida.dto.response.AreaAcademicaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ComplementacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteHistoriaAcademicaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.InformacionAdicionalDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.InvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDocenteDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;

public class HistoriaAcademicaMapper {

        private static final DateTimeFormatter FECHA_GRADO_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        private HistoriaAcademicaMapper() {
        }

        public static AsignaturaCursadaDTO toAsignaturaDto(AsignaturaCursadaResumen asignatura) {
                String periodo = asignatura.getAnio() == null || asignatura.getPeriodo() == null
                                ? null
                                : asignatura.getAnio() + "-" + asignatura.getPeriodo();
                String codigoMateria = asignatura.getCodigoAsignatura() == null
                                ? null
                                : String.valueOf(asignatura.getCodigoAsignatura());
                String notaDefinitiva = asignatura.getNota() == null
                                ? null
                                : asignatura.getNota().stripTrailingZeros().toPlainString();

                return AsignaturaCursadaDTO.builder()
                                .periodoCursado(periodo)
                                .codigoMateria(codigoMateria)
                                .nombreMateria(asignatura.getNombreAsignatura())
                                .creditos(asignatura.getCreditos())
                                .notaDefinitiva(notaDefinitiva)
                                .build();
        }

        public static PasantiaInvestigacionDTO toPasantiaDto(PasantiaInvestigacion pasantia) {
                return PasantiaInvestigacionDTO.builder()
                                .creditosAsignados(pasantia.getCreditosAsignados())
                                .acta(pasantia.getActa())
                                .fechaActa(pasantia.getFechaActa())
                                .informePasantia(pasantia.getInformePasantia())
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
                                .horas(practica.getHoras())
                                .build();
        }

        public static HistoriaAcademicaResponseDTO toHistoriaAcademicaResponse(
                        Estudiante estudiante,
                        List<AsignaturaCursadaDTO> fundamentacion,
                        List<AsignaturaCursadaDTO> electivas,
                        List<AsignaturaCursadaDTO> investigacionAsignaturas,
                        List<PasantiaInvestigacionDTO> pasantias,
                        List<PublicacionInvestigacionDTO> publicaciones,
                        List<PracticaDocenteDTO> practicasDocentes,
                        List<AsignaturaCursadaDTO> competenciasEmpresariales,
                        Integer creditosCumplidos,
                        String tituloTesis,
                        String directorTesis,
                        String codirectorTesis) {

                String nombreCompleto = (estudiante.getPersona().getNombre() + " "
                                + estudiante.getPersona().getApellido()).trim();
                String fechaGrado = estudiante.getFechaGrado() == null ? null
                                : estudiante.getFechaGrado().format(FECHA_GRADO_FORMATTER);

                AreaAcademicaDTO fundamentacionArea = AreaAcademicaDTO.builder()
                                .asignaturas(fundamentacion)
                                .build();
                AreaAcademicaDTO electivasArea = AreaAcademicaDTO.builder()
                                .asignaturas(electivas)
                                .build();
                AreaAcademicaDTO competenciasEmpresarialesArea = AreaAcademicaDTO.builder()
                                .asignaturas(competenciasEmpresariales)
                                .build();

                InvestigacionDTO investigacion = InvestigacionDTO.builder()
                                .asignaturas(investigacionAsignaturas)
                                .pasantias(pasantias)
                                .publicaciones(publicaciones)
                                .build();

                ComplementacionDTO complementacion = ComplementacionDTO.builder()
                                .practicasDocentes(practicasDocentes)
                                .competenciasEmpresariales(competenciasEmpresarialesArea)
                                .build();

                InformacionAdicionalDTO informacionAdicional = InformacionAdicionalDTO.builder()
                                .creditosCumplidos(creditosCumplidos)
                                .tituloTesis(tituloTesis)
                                .directorTesis(directorTesis)
                                .codirectorTesis(codirectorTesis)
                                .build();

                EstudianteHistoriaAcademicaDTO estudianteDto = EstudianteHistoriaAcademicaDTO.builder()
                                .codigoEstudiante(estudiante.getCodigo())
                                .nombreCompleto(nombreCompleto)
                                .correoUniversidad(estudiante.getCorreoUniversidad())
                                .tituloPregrado(estudiante.getTituloPregrado())
                                .fechaGrado(fechaGrado)
                                .build();

                HistoriaAcademicaDTO historiaAcademica = HistoriaAcademicaDTO.builder()
                                .fundamentacion(fundamentacionArea)
                                .electivas(electivasArea)
                                .investigacion(investigacion)
                                .complementacion(complementacion)
                                .informacionAdicional(informacionAdicional)
                                .build();

                return HistoriaAcademicaResponseDTO.builder()
                                .estudiante(estudianteDto)
                                .historiaAcademica(historiaAcademica)
                                .build();
        }
}
