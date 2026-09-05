package com.maestria.gestion.hoja_de_vida.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarDistincionRequestDTO {

    @NotBlank(message = "El número de resolución es obligatorio.")
    @Size(max = 100, message = "El número de resolución no puede superar los 100 caracteres.")
    @Schema(description = "Nuevo número institucional de la resolución.", example = "RES-TEST-A-002")
    private String numeroResolucion;

    @NotNull(message = "La fecha de resolución es obligatoria.")
    @PastOrPresent(message = "La fecha de resolución no puede ser futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Nueva fecha de expedición en formato ISO.", example = "2026-08-25")
    private LocalDate fechaResolucion;

    private MultipartFile resolucion;
}
