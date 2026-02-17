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
public class AsignaturaCursada {

    private Long id;
    private Long idEstudiante;
    private String periodoCursado;
    private String codigoMateria;
    private String nombreMateria;
    private Integer creditos;
    private String notaDefinitiva;
    private String area;
}
