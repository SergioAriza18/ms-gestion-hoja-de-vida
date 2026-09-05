package com.maestria.gestion.hoja_de_vida.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.Pasantia;
import com.maestria.gestion.hoja_de_vida.domain.Practica;
import com.maestria.gestion.hoja_de_vida.domain.Publicacion;
import com.maestria.gestion.hoja_de_vida.dto.response.AreaAcademicaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.ComplementacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.EstudianteHistoriaAcademicaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.HistoriaAcademicaResponseDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.InformacionAdicionalDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.InvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PasantiaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionDTO;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_A;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.APROXIMACION_NOTA_INSTITUCIONAL;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.ESCALA_NOTA_MOSTRADA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_APROBATORIA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_MAXIMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_MINIMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_NA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_NR;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaRules.esMateriaEspecial;

public class HistoriaAcademicaMapper {

        private HistoriaAcademicaMapper() {
        }

        public static AsignaturaCursadaDTO toAsignaturaDto(AsignaturaCursadaResumen asignatura) {
                String periodo = asignatura.getAnio() == null || asignatura.getPeriodo() == null
                                ? null
                                : asignatura.getAnio() + "-" + asignatura.getPeriodo();
                String codigoMateria = asignatura.getCodigoAsignatura();
                String notaDefinitiva = mapearNotaDefinitiva(codigoMateria, asignatura.getNombreAsignatura(),
                                asignatura.getNota());

                return AsignaturaCursadaDTO.builder()
                                .periodoCursado(periodo)
                                .codigoMateria(codigoMateria)
                                .nombreMateria(asignatura.getNombreAsignatura())
                                .creditos(asignatura.getCreditos())
                                .notaDefinitiva(notaDefinitiva)
                                .build();
        }

        private static String mapearNotaDefinitiva(String codigoMateria, String nombreMateria, BigDecimal nota) {
                if (esMateriaEspecial(codigoMateria, nombreMateria)) {
                        if (nota == null) {
                                return NOTA_NR;
                        }
                        if (nota.compareTo(NOTA_MINIMA) >= 0 && nota.compareTo(NOTA_APROBATORIA) < 0) {
                                return NOTA_NA;
                        }
                        if (nota.compareTo(NOTA_APROBATORIA) >= 0 && nota.compareTo(NOTA_MAXIMA) <= 0) {
                                return NOTA_A;
                        }
                }

                return nota == null ? NOTA_NR : formatearNota(nota);
        }

        private static String formatearNota(BigDecimal nota) {
                return nota.setScale(ESCALA_NOTA_MOSTRADA, APROXIMACION_NOTA_INSTITUCIONAL).toPlainString();
        }

        public static PasantiaDTO toPasantiaDto(Pasantia pasantia) {
                return PasantiaDTO.builder()
                                .creditosAsignados(pasantia.getCreditosAsignados())
                                .acta(pasantia.getActa())
                                .fechaActa(pasantia.getFechaActa())
                                .informePasantia(pasantia.getInformePasantia())
                                .build();
        }

        public static PublicacionDTO toPublicacionDto(Publicacion publicacion) {
                return PublicacionDTO.builder()
                                .creditosAsignados(publicacion.getCreditosAsignados())
                                .acta(publicacion.getActa())
                                .nombrePublicacion(publicacion.getNombrePublicacion())
                                .tipoPublicacion(publicacion.getTipoPublicacion())
                                .categoriaIndexada(publicacion.getCategoriaIndexada())
                                .fechaAceptacion(publicacion.getFechaAceptacion())
                                .urlPublicacion(publicacion.getUrlPublicacion())
                                .build();
        }

        public static PracticaDTO toPracticaDto(Practica practica) {
                return PracticaDTO.builder()
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
                        List<PasantiaDTO> pasantias,
                        List<PublicacionDTO> publicaciones,
                        List<PracticaDTO> practicasDocentes,
                        List<AsignaturaCursadaDTO> competenciasEmpresariales,
                        Integer creditosCumplidos,
                        BigDecimal promedioCarrera,
                        String tituloTesis,
                        String directorTesis,
                        String codirectorTesis,
                        List<AsignaturaCursadaDTO> requisitosGrado,
                        List<String> distincionesAcademicas) {

                String nombreCompleto = (estudiante.getPersona().getNombre() + " "
                                + estudiante.getPersona().getApellido()).trim();

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
                                .asignaturas(requisitosGrado)
                                .distincionesAcademicas(distincionesAcademicas)
                                .build();

                EstudianteHistoriaAcademicaDTO estudianteDto = EstudianteHistoriaAcademicaDTO.builder()
                                .codigoEstudiante(estudiante.getCodigo())
                                .nombreCompleto(nombreCompleto)
                                .identificacion(toStringOrNull(estudiante.getPersona().getIdentificacion()))
                                .correoUniversidad(estudiante.getCorreoUniversidad())
                                .periodoIngreso(estudiante.getPeriodoIngreso())
                                .semestreAcademico(estudiante.getSemestreAcademico())
                                .promedioCarrera(promedioCarrera)
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

        private static String toStringOrNull(Long value) {
                return value == null ? null : value.toString();
        }
}
