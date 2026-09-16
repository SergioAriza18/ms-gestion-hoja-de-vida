package com.maestria.gestion.hoja_de_vida.common;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_MAXIMA;
import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaConstants.NOTA_MINIMA;

import java.math.BigDecimal;
import java.util.Locale;

public final class HistoriaAcademicaRules {

    private HistoriaAcademicaRules() {
    }

    public static boolean esMateriaEspecial(String codigoMateria, String nombreMateria) {
        return coincideCodigoEspecial(codigoMateria) || coincideCodigoEspecial(nombreMateria);
    }

    public static boolean esCalificacionValida(Double calificacion) {
        if (calificacion == null || !Double.isFinite(calificacion)) {
            return false;
        }

        BigDecimal nota = BigDecimal.valueOf(calificacion);
        return nota.compareTo(NOTA_MINIMA) >= 0 && nota.compareTo(NOTA_MAXIMA) <= 0;
    }

    private static boolean coincideCodigoEspecial(String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }

        String normalizado = valor.trim().toUpperCase(Locale.ROOT);
        return HistoriaAcademicaConstants.CODIGOS_MATERIAS_ESPECIALES.contains(normalizado);
    }
}
