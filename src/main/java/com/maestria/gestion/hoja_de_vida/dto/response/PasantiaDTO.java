package com.maestria.gestion.hoja_de_vida.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasantiaDTO {

    private Integer creditosAsignados;
    private String acta;
    private String fechaActa;
    private String informePasantia;
}
