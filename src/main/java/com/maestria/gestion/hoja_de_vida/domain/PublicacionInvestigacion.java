package com.maestria.gestion.hoja_de_vida.domain;

import java.time.LocalDate;

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
@Table(name = "publicaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicacionInvestigacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creditospub")
    private Integer creditosAsignados;

    @Column(name = "numactapub")
    private String acta;

    @Column(name = "titulopubli", nullable = false)
    private String nombrePublicacion;

    @Column(name = "tipopub", nullable = false)
    private String tipoPublicacion;

    @Column(name = "indexadapub")
    private String categoriaIndexada;

    @Column(name = "fechaaceptacion")
    private LocalDate fechaAceptacion;
}
