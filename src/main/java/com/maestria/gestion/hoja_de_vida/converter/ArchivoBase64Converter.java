package com.maestria.gestion.hoja_de_vida.converter;

import java.util.Base64;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ArchivoBase64Converter implements AttributeConverter<byte[], String> {

    @Override
    public String convertToDatabaseColumn(byte[] contenido) {
        return contenido == null ? null : Base64.getEncoder().encodeToString(contenido);
    }

    @Override
    public byte[] convertToEntityAttribute(String contenidoBase64) {
        if (contenidoBase64 == null) {
            return null;
        }

        try {
            return Base64.getDecoder().decode(contenidoBase64);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("El archivo almacenado no contiene un Base64 válido.", ex);
        }
    }
}
