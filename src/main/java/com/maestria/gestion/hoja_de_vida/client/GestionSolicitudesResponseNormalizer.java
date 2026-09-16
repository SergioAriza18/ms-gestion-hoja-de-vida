package com.maestria.gestion.hoja_de_vida.client;

import static com.maestria.gestion.hoja_de_vida.common.HistoriaAcademicaRules.esCalificacionValida;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCanceladaDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaHomologadaDTO;

@Component
public class GestionSolicitudesResponseNormalizer {

    private static final Pattern FORMATO_PERIODO = Pattern.compile("^\\d{4}-[12]$");

    public List<AsignaturaHomologadaDTO> normalizarHomologaciones(AsignaturaHomologadaDTO[] respuesta) {
        if (respuesta == null) {
            return List.of();
        }

        return Arrays.stream(respuesta)
                .map(this::normalizarHomologacion)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<AsignaturaCanceladaDTO> normalizarCancelaciones(AsignaturaCanceladaDTO[] respuesta) {
        if (respuesta == null) {
            return List.of();
        }

        return Arrays.stream(respuesta)
                .map(this::normalizarCancelacion)
                .filter(Objects::nonNull)
                .toList();
    }

    private AsignaturaHomologadaDTO normalizarHomologacion(AsignaturaHomologadaDTO asignatura) {
        if (asignatura == null) {
            return null;
        }

        String nombreAsignatura = normalizarTexto(asignatura.getNombreAsignatura());
        if (nombreAsignatura == null) {
            return null;
        }

        return AsignaturaHomologadaDTO.builder()
                .nombreAsignatura(nombreAsignatura)
                .creditos(normalizarCreditos(asignatura.getCreditos()))
                .calificacion(normalizarCalificacion(asignatura.getCalificacion()))
                .programaProcedencia(normalizarTexto(asignatura.getProgramaProcedencia()))
                .institucionProcedencia(normalizarTexto(asignatura.getInstitucionProcedencia()))
                .build();
    }

    private AsignaturaCanceladaDTO normalizarCancelacion(AsignaturaCanceladaDTO asignatura) {
        if (asignatura == null) {
            return null;
        }

        String nombreAsignatura = normalizarTexto(asignatura.getNombreAsignatura());
        String periodoCancelacion = normalizarTexto(asignatura.getPeriodoCancelacion());
        if (nombreAsignatura == null
                || periodoCancelacion == null
                || !FORMATO_PERIODO.matcher(periodoCancelacion).matches()) {
            return null;
        }

        return AsignaturaCanceladaDTO.builder()
                .nombreAsignatura(nombreAsignatura)
                .grupo(normalizarTexto(asignatura.getGrupo()))
                .periodoCancelacion(periodoCancelacion)
                .build();
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.strip();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }

    private Integer normalizarCreditos(Integer creditos) {
        return creditos != null && creditos > 0 ? creditos : null;
    }

    private Double normalizarCalificacion(Double calificacion) {
        return esCalificacionValida(calificacion) ? calificacion : null;
    }
}
