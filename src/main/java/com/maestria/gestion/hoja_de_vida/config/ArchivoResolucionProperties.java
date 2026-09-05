package com.maestria.gestion.hoja_de_vida.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "app.archivos.resolucion")
public class ArchivoResolucionProperties {

    @NotNull
    private DataSize tamanoMaximo;

    public DataSize getTamanoMaximo() {
        return tamanoMaximo;
    }

    public void setTamanoMaximo(DataSize tamanoMaximo) {
        this.tamanoMaximo = tamanoMaximo;
    }
}
