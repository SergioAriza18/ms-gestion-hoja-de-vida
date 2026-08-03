package com.maestria.gestion.hoja_de_vida.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstudianteBusquedaDTO {

    private String codigo;
    private String nombre;
    private String apellido;
    private String periodoIngreso;
    private String identificacion;
}
