package com.maestria.gestion.hoja_de_vida.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "correo_universidad")
    private String correoUniversidad;

    @Column(name = "semestre_academico")
    private Integer semestreAcademico;

    @Column(name = "periodo_ingreso")
    private String periodoIngreso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_maestria")
    private EstadoMaestria estadoMaestria;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidad")
    private ModalidadAcademica modalidadAcademica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo_investigacion")
    private GrupoInvestigacion grupoInvestigacion;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona;
}
