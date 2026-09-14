package com.maestria.gestion.hoja_de_vida.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaHomologadaDTO {

    private String nombreAsignatura;
    private Integer creditos;
    private Double calificacion;
    private String programaProcedencia;
    private String institucionProcedencia;
}
