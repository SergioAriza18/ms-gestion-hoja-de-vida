package com.maestria.gestion.hoja_de_vida.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticaDTO {

    private Integer creditosAsignados;
    private String acta;
    private LocalDate fechaActa;
    private Integer horas;
    private List<ActividadPracticaDTO> actividades;
}
