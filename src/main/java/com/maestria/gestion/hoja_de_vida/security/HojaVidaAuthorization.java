package com.maestria.gestion.hoja_de_vida.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("hojaVidaAuthorization")
public class HojaVidaAuthorization {

    private static final String ROLE_COORDINADOR = "ROLE_COORDINADOR";
    private static final String ROLE_ESTUDIANTE = "ROLE_ESTUDIANTE";
    private static final String CLAIM_CODIGO_ACADEMICO = "codigoAcademico";

    public boolean puedeConsultar(Authentication authentication, String codigoEstudiante) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (tieneRol(authentication, ROLE_COORDINADOR)) {
            return true;
        }

        if (!tieneRol(authentication, ROLE_ESTUDIANTE)
                || !(authentication instanceof JwtAuthenticationToken)) {
            return false;
        }

        Object codigoAcademico = ((JwtAuthenticationToken) authentication)
                .getTokenAttributes()
                .get(CLAIM_CODIGO_ACADEMICO);
        return codigoAcademico != null && codigoEstudiante.equals(codigoAcademico.toString());
    }

    private boolean tieneRol(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> role.equals(authority.getAuthority()));
    }
}
