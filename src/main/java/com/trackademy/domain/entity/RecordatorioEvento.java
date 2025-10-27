package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recordatorio_evento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecordatorioEvento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(length = 50)
    private String tipo; // p.ej. EVALUACION, HABITO

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(name = "fecha_programada", nullable = false)
    private LocalDateTime fechaProgramada;

    @Column(nullable = false)
    private Boolean enviado = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_evaluacion_id")
    private UsuarioEvaluacion usuarioEvaluacion; // si aplica
}

