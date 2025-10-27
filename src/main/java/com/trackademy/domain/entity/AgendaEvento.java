package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agenda_evento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AgendaEvento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fin")
    private LocalDateTime fin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso; // opcional

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_evaluacion_id")
    private UsuarioEvaluacion usuarioEvaluacion; // opcional
}

