package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curso_competencia", uniqueConstraints = {
        @UniqueConstraint(name = "uk_curso_comp", columnNames = {"curso_id", "competencia_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CursoCompetencia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "competencia_id")
    private Competencia competencia;
}

