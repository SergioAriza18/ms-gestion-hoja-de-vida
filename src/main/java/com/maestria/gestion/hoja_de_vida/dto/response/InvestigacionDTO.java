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
public class InvestigacionDTO {

    private List<AsignaturaCursadaDTO> asignaturasVistas;
    private List<PasantiaInvestigacionDTO> pasantias;
    private List<PublicacionInvestigacionDTO> publicaciones;
}
