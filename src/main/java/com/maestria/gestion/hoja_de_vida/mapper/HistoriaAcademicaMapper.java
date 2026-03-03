package com.maestria.gestion.hoja_de_vida.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.maestria.gestion.hoja_de_vida.domain.Estudiante;
import com.maestria.gestion.hoja_de_vida.domain.PasantiaInvestigacion;
import com.maestria.gestion.hoja_de_vida.domain.Practica;
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
import com.maestria.gestion.hoja_de_vida.dto.response.PracticaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PublicacionInvestigacionDTO;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;

public class HistoriaAcademicaMapper {

        private static final BigDecimal NOTA_NO_APROBADA = BigDecimal.ZERO;
        private static final BigDecimal NOTA_APROBADA = BigDecimal.valueOf(5);
        private static final String NOTA_NA = "NA";
        private static final String NOTA_A = "A";
        private static final String NOTA_NR = "NR";
        private static final Set<String> CODIGOS_NOTA_ESPECIAL = Set.of(
                        "PSI POSG_MC",
                        "M27706",
                        "M27708",
                        "M27709");

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
                if (esMateriaConReglaEspecial(codigoMateria, nombreMateria)) {
                        if (nota == null) {
                                return NOTA_NR;
                        }
                        if (nota.compareTo(NOTA_NO_APROBADA) == 0) {
                                return NOTA_NA;
                        }
                        if (nota.compareTo(NOTA_APROBADA) == 0) {
                                return NOTA_A;
                        }
                }

                return nota == null ? null : nota.stripTrailingZeros().toPlainString();
        }

        private static boolean esMateriaConReglaEspecial(String codigoMateria, String nombreMateria) {
                return coincideCodigoEspecial(codigoMateria) || coincideCodigoEspecial(nombreMateria);
        }

        private static boolean coincideCodigoEspecial(String valor) {
                if (valor == null || valor.isBlank()) {
                        return false;
                }

                String normalizado = valor.trim().toUpperCase(Locale.ROOT);
                return CODIGOS_NOTA_ESPECIAL.contains(normalizado);
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
                                .categoriaIndexada(publicacion.getCategoriaIndexada())
                                .fechaAceptacion(publicacion.getFechaAceptacion())
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
                        List<PasantiaInvestigacionDTO> pasantias,
                        List<PublicacionInvestigacionDTO> publicaciones,
                        List<PracticaDTO> practicasDocentes,
                        List<AsignaturaCursadaDTO> competenciasEmpresariales,
                        Integer creditosCumplidos,
                        String tituloTesis,
                        String directorTesis,
                        String codirectorTesis,
                        List<AsignaturaCursadaDTO> requisitosGrado) {

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
                                .build();

                EstudianteHistoriaAcademicaDTO estudianteDto = EstudianteHistoriaAcademicaDTO.builder()
                                .codigoEstudiante(estudiante.getCodigo())
                                .nombreCompleto(nombreCompleto)
                                .identificacion(estudiante.getPersona().getIdentificacion())
                                .correoUniversidad(estudiante.getCorreoUniversidad())
                                .periodoIngreso(estudiante.getPeriodoIngreso())
                                .semestreAcademico(estudiante.getSemestreAcademico())
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
