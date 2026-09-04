package com.maestria.gestion.hoja_de_vida.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResolucionDistincionDTO {

    private final String nombreArchivo;
    private final byte[] contenido;
}
