package com.maestria.gestion.hoja_de_vida.dto.response;

import java.time.Instant;

import com.maestria.gestion.hoja_de_vida.security.demo.PerfilAccesoDemo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SesionDemoResponseDTO {

    private String accessToken;
    private String tokenType;
    private Instant expiresAt;
    private PerfilAccesoDemo perfil;
    private String nombre;
    private String rol;
    private String codigoAcademico;
}
