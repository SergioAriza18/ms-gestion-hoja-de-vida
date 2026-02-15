package com.maestria.gestion.hoja_de_vida.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasantiaInvestigacionDTO {

    private Integer creditosAsignados;
    private String acta;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String pais;
    private String ciudad;
    private String universidad;
}
