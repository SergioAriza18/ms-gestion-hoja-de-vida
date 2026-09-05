package com.maestria.gestion.hoja_de_vida.exception;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta estándar para errores de validación, autenticación y negocio.")
public class ApiError {

    @Schema(description = "Fecha y hora del error con zona horaria.")
    private final OffsetDateTime fechaHora;

    @Schema(description = "Código de estado HTTP.")
    private final int estado;

    @Schema(description = "Código estable del tipo de error.")
    private final String codigo;

    @Schema(description = "Descripción útil del error.")
    private final String mensaje;

    @Schema(description = "Ruta donde ocurrió el error.")
    private final String url;
}
