package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "usuario_curso_resumen")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioCursoResumen {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_curso_id", nullable = false, unique = true)
    private UsuarioCurso usuarioCurso;

    @Column(name = "promedio_parcial", precision = 5, scale = 2)
    private BigDecimal promedioParcial;

    @Column(name = "nota_necesaria", precision = 5, scale = 2)
    private BigDecimal notaNecesaria;

    @Column(name = "progreso_porcentaje", precision = 6, scale = 2)
    private BigDecimal progresoPorcentaje;

    @Column(name = "nota_final", precision = 6, scale = 2)
    private BigDecimal notaFinal;

    @Column
    private Integer creditos;

    @Column(name = "riesgo_aplazo_score", precision = 6, scale = 2)
    private BigDecimal riesgoAplazoScore;

    @Column(name = "siguiente_hito_semana")
    private Integer siguienteHitoSemana;

    @Column(name = "siguiente_hito_fecha")
    private java.time.LocalDate siguienteHitoFecha;
}
