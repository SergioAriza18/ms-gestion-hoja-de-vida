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
@Table(name = "publicaciones_investigacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicacionInvestigacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @Column(name = "creditos_asignados", nullable = false)
    private Integer creditosAsignados;

    @Column(name = "acta", nullable = false)
    private String acta;

    @Column(name = "nombre_publicacion", nullable = false)
    private String nombrePublicacion;

    @Column(name = "tipo_publicacion", nullable = false)
    private String tipoPublicacion;

    @Column(name = "fecha_aceptacion")
    private LocalDate fechaAceptacion;
}
