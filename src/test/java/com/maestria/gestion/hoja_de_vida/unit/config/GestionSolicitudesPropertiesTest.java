package com.maestria.gestion.hoja_de_vida.unit.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.maestria.gestion.hoja_de_vida.config.GestionSolicitudesProperties;

@DisplayName("Validación de la configuración del servicio de solicitudes")
class GestionSolicitudesPropertiesTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Debe aceptar una configuración completa")
    void aceptaConfiguracionCompleta() {
        GestionSolicitudesProperties properties = crearProperties(
                "http://localhost:8095/msmaestriac",
                Duration.ofSeconds(5),
                Duration.ofSeconds(15));

        assertThat(validator.validate(properties)).isEmpty();
    }

    @Test
    @DisplayName("Debe rechazar valores obligatorios ausentes")
    void rechazaValoresObligatoriosAusentes() {
        GestionSolicitudesProperties properties = crearProperties(" ", null, null);

        assertThat(validator.validate(properties))
                .extracting(violacion -> violacion.getPropertyPath().toString())
                .containsExactlyInAnyOrder("url", "connectTimeout", "readTimeout");
    }

    private GestionSolicitudesProperties crearProperties(
            String url,
            Duration connectTimeout,
            Duration readTimeout) {
        GestionSolicitudesProperties properties = new GestionSolicitudesProperties();
        properties.setUrl(url);
        properties.setConnectTimeout(connectTimeout);
        properties.setReadTimeout(readTimeout);
        return properties;
    }
}
