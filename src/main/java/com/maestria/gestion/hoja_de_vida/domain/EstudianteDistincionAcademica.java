package com.maestria.gestion.hoja_de_vida.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "estudiantes_distinciones_academicas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_estudiante_distincion_academica",
                columnNames = { "id_estudiante", "id_distincion_academica" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDistincionAcademica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_distincion_academica", nullable = false)
    private DistincionAcademica distincion;

    @Column(name = "numero_resolucion", nullable = false, length = 100)
    private String numeroResolucion;

    @Column(name = "fecha_resolucion", nullable = false)
    private LocalDate fechaResolucion;

    @Lob
    @Column(name = "resolucion_pdf", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] resolucionPdf;
}
