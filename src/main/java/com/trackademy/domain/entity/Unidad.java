package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidad")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Unidad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false, length = 200)
    private String titulo;
}

