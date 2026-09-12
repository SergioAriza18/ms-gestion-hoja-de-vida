package com.maestria.gestion.hoja_de_vida.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "practicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @Column(name = "creditosprac", nullable = false)
    private Integer creditosAsignados;

    @Column(name = "numactaprac", nullable = false)
    private String acta;

    @Column(name = "fechaactaprac")
    private LocalDate fechaActa;

    @Column(name = "horastotales")
    private Integer horas;

    @OneToMany
    @JoinColumn(name = "idpractica", referencedColumnName = "id", insertable = false, updatable = false)
    @OrderBy("id ASC")
    @Builder.Default
    private List<ActividadPractica> actividades = new ArrayList<>();
}
