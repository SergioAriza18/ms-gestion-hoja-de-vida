package com.maestria.gestion.hoja_de_vida.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matricula_calificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignaturaCursada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_matricula", nullable = false)
    private Integer idMatricula;

    @Column(name = "id_asignatura", nullable = false)
    private Long idAsignatura;

    @Column(name = "nota", nullable = false, precision = 3, scale = 2)
    private BigDecimal nota;

    @Column(name = "es_definitiva")
    private Boolean esDefinitiva;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
}
