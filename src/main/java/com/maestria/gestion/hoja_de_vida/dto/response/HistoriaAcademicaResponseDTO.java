package com.maestria.gestion.hoja_de_vida.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaAcademicaResponseDTO {

    private String codigoEstudiante;
    private List<AsignaturaCursadaDTO> fundamentacion;
    private List<AsignaturaCursadaDTO> electivas;
    private InvestigacionDTO investigacion;
    private ComplementacionDTO complementacion;
}
