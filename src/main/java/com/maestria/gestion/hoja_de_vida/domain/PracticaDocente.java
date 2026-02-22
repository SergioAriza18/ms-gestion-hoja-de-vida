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
@Table(name = "practicas") //NO TODAS SON DOCENTE
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticaDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @Column(name = "creditosprac", nullable = false)
    private Integer creditosAsignados;

    @Column(name = "numactaprac", nullable = false)
    private String acta;

    @Column(name = "horastotales")
    private Integer horas;
}
