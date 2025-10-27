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

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false)
    private Integer semana;

    @Column(nullable = false)
    private Integer porcentaje;
}

