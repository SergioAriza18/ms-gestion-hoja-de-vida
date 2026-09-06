package com.maestria.gestion.hoja_de_vida.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.maestria.gestion.hoja_de_vida.security.SecurityErrorHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final int MINIMUM_HS512_KEY_BYTES = 64;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityErrorHandler securityErrorHandler,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            AccesoDemoProperties accesoDemoProperties,
            Environment environment) throws Exception {
        boolean accesoDemoHabilitado = accesoDemoProperties.isEnabled()
                && environment.acceptsProfiles(Profiles.of("demo"))
                && !environment.acceptsProfiles(Profiles.of("prod"));

        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorize -> {
                    authorize.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    authorize.antMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**")
                            .permitAll();
                    if (accesoDemoHabilitado) {
                        authorize.antMatchers("/api/demo/auth/**").permitAll();
                    }
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(securityErrorHandler)
                        .accessDeniedHandler(securityErrorHandler))
                .oauth2ResourceServer(oauth -> oauth
                        .authenticationEntryPoint(securityErrorHandler)
                        .accessDeniedHandler(securityErrorHandler)
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

    @Bean
    public SecretKey jwtSecretKey(JwtSecurityProperties properties) {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(properties.getSecret());
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("HOJA_VIDA_JWT_SECRET debe ser una clave codificada en Base64.", ex);
        }

        if (keyBytes.length < MINIMUM_HS512_KEY_BYTES) {
            throw new IllegalStateException(
                    "HOJA_VIDA_JWT_SECRET debe representar al menos 64 bytes para utilizar HS512.");
        }

        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("rol");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }
}
