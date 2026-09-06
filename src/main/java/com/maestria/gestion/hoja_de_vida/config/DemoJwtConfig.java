package com.maestria.gestion.hoja_de_vida.config;

import javax.crypto.SecretKey;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@Profile("demo & !prod")
@ConditionalOnProperty(prefix = "app.demo-auth", name = "enabled", havingValue = "true")
public class DemoJwtConfig {

    @Bean
    public JwtEncoder demoJwtEncoder(SecretKey jwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
    }
}
