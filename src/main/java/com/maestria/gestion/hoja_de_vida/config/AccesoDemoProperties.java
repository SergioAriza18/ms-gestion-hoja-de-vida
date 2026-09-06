package com.maestria.gestion.hoja_de_vida.config;

import java.time.Duration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.AssertTrue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "app.demo-auth")
public class AccesoDemoProperties {

    private static final Duration MINIMUM_TOKEN_VALIDITY = Duration.ofMinutes(5);
    private static final Duration MAXIMUM_TOKEN_VALIDITY = Duration.ofHours(12);

    private boolean enabled;

    @NotNull
    private Duration tokenValidity = Duration.ofHours(4);

    @NotBlank
    private String coordinatorName = "Coordinador de demostración";

    @NotBlank
    private String studentName = "Ana - estudiante de demostración";

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9-]+$")
    private String studentCode = "IS20260157";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Duration getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(Duration tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    @AssertTrue(message = "La vigencia del token de demostración debe estar entre 5 minutos y 12 horas.")
    public boolean isTokenValidityValid() {
        return tokenValidity != null
                && tokenValidity.compareTo(MINIMUM_TOKEN_VALIDITY) >= 0
                && tokenValidity.compareTo(MAXIMUM_TOKEN_VALIDITY) <= 0;
    }
}
