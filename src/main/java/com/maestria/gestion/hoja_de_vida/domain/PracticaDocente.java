package com.maestria.gestion.hoja_de_vida.domain;

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
public class PracticaDocente {

    private Long id;
    private Long idEstudiante;
    private Integer creditosAsignados;
    private String acta;
    private Integer numeroActividades;
}
