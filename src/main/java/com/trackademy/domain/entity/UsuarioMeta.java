package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_meta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioMeta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "fecha_objetivo")
    private LocalDate fechaObjetivo;

    @Column
    private Integer progreso; // 0-100

    @Column(columnDefinition = "text")
    private String tipo; // rendimiento | habitos | asistencia | proyecto

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(columnDefinition = "text")
    private String estado; // activa | pausada | completada
}
