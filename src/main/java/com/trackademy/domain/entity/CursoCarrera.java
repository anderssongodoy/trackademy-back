package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curso_carrera", uniqueConstraints = {
        @UniqueConstraint(name = "uk_curso_carrera", columnNames = {"curso_id", "carrera_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CursoCarrera {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;
}

