package com.maestria.gestion.hoja_de_vida.exception;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {

    private final OffsetDateTime fechaHora;
    private final int estado;
    private final String codigo;
    private final String mensaje;
    private final String url;
}
