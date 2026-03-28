package com.maestria.gestion.hoja_de_vida.domain;

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
@Table(name = "pasantias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pasantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @Column(name = "creditospas", nullable = false)
    private Integer creditosAsignados;

    @Column(name = "numactapas", nullable = false)
    private String acta;

    @Column(name = "fechaactapas")
    private String fechaActa;

    @Column(name = "informepasantia")
    private String informePasantia;

    @Column(name = "estado")
    private Boolean estado;
}
