package com.maestria.gestion.hoja_de_vida.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discapacidad")
    private String discapacidad;

    @Column(name = "etnia")
    private String etnia;

    @Column(name = "tipo_poblacion")
    private String tipoPoblacion;

    @Column(name = "ciudad_residencia")
    private String ciudadResidencia;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "correo_universidad")
    private String correoUniversidad;

    @Column(name = "fecha_grado")
    private LocalDate fechaGrado;

    @Column(name = "cohorte")
    private Integer cohorte;

    @Column(name = "es_estudiante_doctorado")
    private String esEstudianteDoctorado;

    @Column(name = "estado_maestria")
    private String estadoMaestria;

    @Column(name = "modalidad")
    private String modalidad;

    @Column(name = "modalidad_ingreso")
    private String modalidadIngreso;

    @Column(name = "periodo_ingreso")
    private String periodoIngreso;

    @Column(name = "semestre_academico")
    private Integer semestreAcademico;

    @Column(name = "semestre_financiero")
    private Integer semestreFinanciero;

    @Column(name = "titulo_doctorado")
    private String tituloDoctorado;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "titulo_pregrado")
    private String tituloPregrado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "usuario_creacion")
    private Integer usuarioCreacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_modificacion")
    private Integer usuarioModificacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}
