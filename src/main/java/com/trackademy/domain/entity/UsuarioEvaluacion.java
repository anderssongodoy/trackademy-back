package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "usuario_evaluacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioEvaluacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_curso_id", nullable = false)
    private UsuarioCurso usuarioCurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacionOriginal;

    @Column(nullable = false)
    private Integer semana;

    @Column(nullable = false)
    private Integer porcentaje;

    @Column(name = "fecha_estimada")
    private LocalDate fechaEstimada;

    @Column(precision = 5, scale = 2)
    private BigDecimal nota; // null si pendiente
}

