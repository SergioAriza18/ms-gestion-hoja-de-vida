package com.maestria.gestion.hoja_de_vida.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasantiaInvestigacion {

    private Long id;
    private Long idEstudiante;
    private Integer creditosAsignados;
    private String acta;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String pais;
    private String ciudad;
    private String universidad;
}
