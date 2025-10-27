package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nota_politica")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotaPolitica {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false, unique = true)
    private Curso curso;

    @Column(nullable = false, columnDefinition = "text")
    private String politica;
}

