package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bibliografia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bibliografia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(nullable = false, columnDefinition = "text")
    private String referencia;
}

