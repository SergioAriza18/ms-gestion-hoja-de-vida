package com.maestria.gestion.hoja_de_vida.dto.response;

import java.time.LocalDate;

import com.maestria.gestion.hoja_de_vida.domain.TipoDistincionAcademica;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistincionAcademicaDetalleDTO {

    private TipoDistincionAcademica tipo;
    private String numeroResolucion;
    private LocalDate fechaResolucion;
}
