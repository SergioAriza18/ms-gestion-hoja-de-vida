package com.maestria.gestion.hoja_de_vida.common;

import java.util.Locale;

public final class HistoriaAcademicaRules {

    private HistoriaAcademicaRules() {
    }

    public static boolean esMateriaEspecial(String codigoMateria, String nombreMateria) {
        return coincideCodigoEspecial(codigoMateria) || coincideCodigoEspecial(nombreMateria);
    }

    private static boolean coincideCodigoEspecial(String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }

        String normalizado = valor.trim().toUpperCase(Locale.ROOT);
        return HistoriaAcademicaConstants.CODIGOS_MATERIAS_ESPECIALES.contains(normalizado);
    }
}
