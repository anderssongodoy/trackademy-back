package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "risk_event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RiskEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_curso_id")
    private UsuarioCurso usuarioCurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(nullable = false, length = 80)
    private String tipo; // bajo_promedio | falta_nota | proximidad_evaluacion | inactividad

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(columnDefinition = "text")
    private String detalle;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;

    @Column
    private Short severidad; // 1..3

    @Column
    private Integer semana;

    @Column(name = "generado_en")
    private java.time.OffsetDateTime generadoEn;

    @Column(name = "data_json", columnDefinition = "text")
    private String dataJson;
}
