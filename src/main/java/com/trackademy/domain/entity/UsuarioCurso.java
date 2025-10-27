package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_curso", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_curso", columnNames = {"usuario_id", "curso_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioCurso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}

