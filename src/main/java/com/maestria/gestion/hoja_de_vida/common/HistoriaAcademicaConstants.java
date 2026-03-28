package com.maestria.gestion.hoja_de_vida.common;

import java.math.BigDecimal;
import java.util.Set;

public final class HistoriaAcademicaConstants {

    // Áreas de formación
    public static final Long AREA_FUNDAMENTACION = 5L;
    public static final Long AREA_ELECTIVAS = 6L;
    public static final Long AREA_INVESTIGACION = 7L;
    public static final Long AREA_COMPLEMENTACION = 8L;
    public static final Long AREA_REQUISITOS_GRADO = 9L;

    // Valores de texto
    public static final String VALOR_TEXTO_VACIO = "";

    // Reglas de nota para asignaturas especiales
    public static final BigDecimal NOTA_NO_APROBADA = BigDecimal.ZERO;
    public static final BigDecimal NOTA_APROBADA = BigDecimal.valueOf(5);
    public static final String NOTA_NA = "NA";
    public static final String NOTA_A = "A";
    public static final String NOTA_NR = "NR";
    public static final Set<String> CODIGOS_MATERIAS_ESPECIALES = Set.of(
            "PSI POSG_MC", // Prueba de suficiencia en Idioma
            "M27708", // Seminario de investigacion
            "M27709", // Trabajo de grado I
            "M27712" // Trabajo de grado II
    );

    private HistoriaAcademicaConstants() {
    }
}
