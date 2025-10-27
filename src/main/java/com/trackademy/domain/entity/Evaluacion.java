package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluacion", uniqueConstraints = {
        @UniqueConstraint(name = "uk_eval_curso_codigo", columnNames = {"curso_id", "codigo"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evaluacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(nullable = false, length = 40)
    private String codigo; // p.ej. PC1, EP, EF

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column
    private Integer semana;

    @Column(precision = 6, scale = 2)
    private java.math.BigDecimal porcentaje;

    @Column(columnDefinition = "text")
    private String tipo; // TA, PC, EXFN, etc.

    @Column(columnDefinition = "text")
    private String observacion;

    @Column(columnDefinition = "text")
    private String modalidad;

    @Column(name = "individual_grupal", columnDefinition = "text")
    private String individualGrupal;

    @Column(columnDefinition = "text")
    private String producto;

    @Column(nullable = false)
    private Boolean flexible = Boolean.FALSE;

    @Column(name = "unidad_nro")
    private Integer unidadNro;

    @Column(name = "atributos_json", columnDefinition = "text")
    private String atributosJson; // usar jsonb en prod
}
