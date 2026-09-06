package com.maestria.gestion.hoja_de_vida.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String[] allowedOrigins;

    @Value("${app.cors.allowed-origin-patterns:}")
    private String[] allowedOriginPatterns;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                CorsRegistration registration = registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders(HttpHeaders.CONTENT_DISPOSITION)
                        .allowCredentials(true);

                String[] patterns = Arrays.stream(allowedOriginPatterns)
                        .map(String::trim)
                        .filter(pattern -> !pattern.isEmpty())
                        .toArray(String[]::new);

                if (patterns.length > 0) {
                    registration.allowedOriginPatterns(patterns);
                }
            }
        };
    }
}
