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
@Table(name = "asignaturas_cursadas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignaturaCursada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @Column(name = "periodo_cursado", nullable = false)
    private String periodoCursado;

    @Column(name = "codigo_materia", nullable = false)
    private String codigoMateria;

    @Column(name = "nombre_materia", nullable = false)
    private String nombreMateria;

    @Column(name = "creditos", nullable = false)
    private Integer creditos;

    @Column(name = "nota_definitiva")
    private String notaDefinitiva;
}
