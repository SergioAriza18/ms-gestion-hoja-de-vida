package com.maestria.gestion.hoja_de_vida.dto.request;

import javax.validation.constraints.NotNull;

import com.maestria.gestion.hoja_de_vida.security.demo.PerfilAccesoDemo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CrearSesionDemoRequestDTO {

    @NotNull(message = "El perfil de demostración es obligatorio.")
    private PerfilAccesoDemo perfil;
}
