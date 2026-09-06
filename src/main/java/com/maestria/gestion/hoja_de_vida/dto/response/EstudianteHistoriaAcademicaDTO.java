package com.maestria.gestion.hoja_de_vida.dto.response;

import java.math.BigDecimal;

import com.maestria.gestion.hoja_de_vida.domain.EstadoMaestria;
import com.maestria.gestion.hoja_de_vida.domain.ModalidadAcademica;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteHistoriaAcademicaDTO {

    private String codigoEstudiante;
    private String nombreCompleto;
    private String identificacion;
    private String correoUniversidad;
    private String periodoIngreso;
    private Integer semestreAcademico;
    private EstadoMaestria estadoMaestria;
    private BigDecimal promedioCarrera;
    private ModalidadAcademica modalidadAcademica;
    private GrupoInvestigacionDTO grupoInvestigacion;
}
