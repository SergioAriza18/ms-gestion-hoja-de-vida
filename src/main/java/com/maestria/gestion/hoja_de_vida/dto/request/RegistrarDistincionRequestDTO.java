package com.maestria.gestion.hoja_de_vida.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarDistincionRequestDTO {

    @NotNull(message = "El tipo de distinción es obligatorio.")
    private TipoDistincionAcademica tipo;

    @NotBlank(message = "El número de resolución es obligatorio.")
    @Size(max = 100, message = "El número de resolución no puede superar los 100 caracteres.")
    private String numeroResolucion;

    @NotNull(message = "La fecha de resolución es obligatoria.")
    @PastOrPresent(message = "La fecha de resolución no puede ser futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaResolucion;

    @NotNull(message = "La resolución en PDF es obligatoria.")
    private MultipartFile resolucion;
}
