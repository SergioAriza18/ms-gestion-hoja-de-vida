package com.maestria.gestion.hoja_de_vida.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentoFirmadoSolicitudDTO {

    private String nombreArchivo;
    private byte[] contenido;
}
