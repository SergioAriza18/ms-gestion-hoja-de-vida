package com.maestria.gestion.hoja_de_vida.security.demo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import com.maestria.gestion.hoja_de_vida.config.AccesoDemoProperties;
import com.maestria.gestion.hoja_de_vida.dto.response.PerfilAccesoDemoDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.SesionDemoResponseDTO;
import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;
import com.maestria.gestion.hoja_de_vida.repository.EstudianteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Profile("demo & !prod")
@ConditionalOnProperty(prefix = "app.demo-auth", name = "enabled", havingValue = "true")
public class AccesoDemoService {

    private static final String ROLE_COORDINADOR = "ROLE_COORDINADOR";
    private static final String ROLE_ESTUDIANTE = "ROLE_ESTUDIANTE";
    private static final String TOKEN_TYPE = "Bearer";
    private static final String ISSUER = "ms-gestion-hoja-de-vida-demo";
    private final AccesoDemoProperties properties;
    private final JwtEncoder jwtEncoder;
    private final EstudianteRepository estudianteRepository;

    public List<PerfilAccesoDemoDTO> listarPerfiles() {
        return List.of(
                perfil(PerfilAccesoDemo.COORDINADOR),
                perfil(PerfilAccesoDemo.ESTUDIANTE));
    }

    public SesionDemoResponseDTO crearSesion(PerfilAccesoDemo perfilSolicitado) {
        PerfilAccesoDemoDTO perfil = perfil(perfilSolicitado);
        validarEstudianteConfigurado(perfil);
        Instant ahora = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiracion = ahora.plus(properties.getTokenValidity());

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .subject(perfilSolicitado.name().toLowerCase() + "@demo.local")
                .issuedAt(ahora)
                .expiresAt(expiracion)
                .claim("rol", List.of(perfil.getRol()))
                .claim("nombre", perfil.getNombre());

        if (perfil.getCodigoAcademico() != null) {
            claims.claim("codigoAcademico", perfil.getCodigoAcademico());
        }

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).type("JWT").build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims.build())).getTokenValue();

        return SesionDemoResponseDTO.builder()
                .accessToken(token)
                .tokenType(TOKEN_TYPE)
                .expiresAt(expiracion)
                .perfil(perfil.getPerfil())
                .nombre(perfil.getNombre())
                .rol(perfil.getRol())
                .codigoAcademico(perfil.getCodigoAcademico())
                .build();
    }

    private PerfilAccesoDemoDTO perfil(PerfilAccesoDemo perfil) {
        if (perfil == PerfilAccesoDemo.COORDINADOR) {
            return PerfilAccesoDemoDTO.builder()
                    .perfil(perfil)
                    .nombre(properties.getCoordinatorName())
                    .rol(ROLE_COORDINADOR)
                    .build();
        }

        return PerfilAccesoDemoDTO.builder()
                .perfil(perfil)
                .nombre(properties.getStudentName())
                .rol(ROLE_ESTUDIANTE)
                .codigoAcademico(properties.getStudentCode())
                .build();
    }

    private void validarEstudianteConfigurado(PerfilAccesoDemoDTO perfil) {
        if (perfil.getPerfil() == PerfilAccesoDemo.ESTUDIANTE
                && estudianteRepository.findByCodigo(perfil.getCodigoAcademico()).isEmpty()) {
            throw new ResourceNotFoundException(
                    "El estudiante configurado para la demostración no existe: "
                            + perfil.getCodigoAcademico());
        }
    }
}
