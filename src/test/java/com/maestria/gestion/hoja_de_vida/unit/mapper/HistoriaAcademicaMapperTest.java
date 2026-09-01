package com.maestria.gestion.hoja_de_vida.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.maestria.gestion.hoja_de_vida.dto.response.AsignaturaCursadaDTO;
import com.maestria.gestion.hoja_de_vida.mapper.HistoriaAcademicaMapper;
import com.maestria.gestion.hoja_de_vida.repository.AsignaturaCursadaRepository.AsignaturaCursadaResumen;

@DisplayName("Pruebas unitarias de HistoriaAcademicaMapper")
class HistoriaAcademicaMapperTest {

    @Test
    @DisplayName("Debe mapear una asignatura normal con nota decimal")
    void toAsignaturaDtoMapeaAsignaturaNormalConNotaDecimal() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M10001", "Arquitectura de software", BigDecimal.valueOf(4.25)));

        assertThat(resultado.getPeriodoCursado()).isEqualTo("2024-1");
        assertThat(resultado.getCodigoMateria()).isEqualTo("M10001");
        assertThat(resultado.getNombreMateria()).isEqualTo("Arquitectura de software");
        assertThat(resultado.getCreditos()).isEqualTo(4);
        assertThat(resultado.getNotaDefinitiva()).isEqualTo("4.25");
    }

    @Test
    @DisplayName("Debe mapear nota nula como nula en asignaturas normales")
    void toAsignaturaDtoMapeaNotaNulaComoNulaEnAsignaturaNormal() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M10002", "Bases de datos", null));

        assertThat(resultado.getNotaDefinitiva()).isNull();
    }

    @Test
    @DisplayName("Debe mapear una materia especial aprobada con nota definitiva A")
    void toAsignaturaDtoMapeaMateriaEspecialAprobadaComoA() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M27708", "Seminario de investigación", BigDecimal.valueOf(5)));

        assertThat(resultado.getNotaDefinitiva()).isEqualTo("A");
    }

    @Test
    @DisplayName("Debe mapear una materia especial con nota menor a 3.5 como NA")
    void toAsignaturaDtoMapeaMateriaEspecialConNotaMenorA35ComoNa() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M27709", "Trabajo de grado I", new BigDecimal("3.4")));

        assertThat(resultado.getNotaDefinitiva()).isEqualTo("NA");
    }

    @Test
    @DisplayName("Debe mapear una materia especial con nota 3.5 como A")
    void toAsignaturaDtoMapeaMateriaEspecialConNota35ComoA() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M27709", "Trabajo de grado I", new BigDecimal("3.5")));

        assertThat(resultado.getNotaDefinitiva()).isEqualTo("A");
    }

    @Test
    @DisplayName("Debe mapear una materia especial sin nota con nota definitiva NR")
    void toAsignaturaDtoMapeaMateriaEspecialSinNotaComoNr() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M27712", "Trabajo de grado II", null));

        assertThat(resultado.getNotaDefinitiva()).isEqualTo("NR");
    }

    @Test
    @DisplayName("Debe agregar decimal .0 cuando la nota es entera")
    void toAsignaturaDtoAgregaDecimalCuandoNotaEsEntera() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M10003", "Analítica de datos", BigDecimal.valueOf(4)));

        assertThat(resultado.getNotaDefinitiva()).isEqualTo("4.0");
    }

    @Test
    @DisplayName("Debe eliminar ceros decimales innecesarios")
    void toAsignaturaDtoEliminaCerosDecimalesInnecesarios() {
        AsignaturaCursadaDTO resultado = HistoriaAcademicaMapper.toAsignaturaDto(
                asignatura("M10004", "Inteligencia artificial", new BigDecimal("4.20")));

        assertThat(resultado.getNotaDefinitiva()).isEqualTo("4.2");
    }

    private AsignaturaCursadaResumen asignatura(String codigo, String nombre, BigDecimal nota) {
        return new AsignaturaCursadaResumen() {
            @Override
            public Integer getAnio() {
                return 2024;
            }

            @Override
            public Integer getPeriodo() {
                return 1;
            }

            @Override
            public String getCodigoAsignatura() {
                return codigo;
            }

            @Override
            public String getNombreAsignatura() {
                return nombre;
            }

            @Override
            public Integer getCreditos() {
                return 4;
            }

            @Override
            public BigDecimal getNota() {
                return nota;
            }

            @Override
            public Long getAreaFormacion() {
                return 7L;
            }
        };
    }
}
