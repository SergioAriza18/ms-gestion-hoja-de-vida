package com.maestria.gestion.hoja_de_vida.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaCursadaDTO {

    private String periodoCursado;
    private String codigoMateria;
    private String nombreMateria;
    private Integer creditos;
    private String notaDefinitiva;
}
