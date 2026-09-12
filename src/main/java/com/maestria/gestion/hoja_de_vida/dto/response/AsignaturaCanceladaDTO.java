package com.maestria.gestion.hoja_de_vida.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaCanceladaDTO {

    private Integer idSolicitud;
    private String nombreAsignatura;
    private String grupo;
    private String periodoCancelacion;
}
