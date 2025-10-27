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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_curso_id")
    private UsuarioCurso usuarioCurso;

    @Column(nullable = false, length = 80)
    private String tipo; // p.ej. PROMEDIO_BAJO, VENCIDAS_SIN_NOTA

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(columnDefinition = "text")
    private String detalle;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;
}

