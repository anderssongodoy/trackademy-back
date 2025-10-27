package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "silabo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Silabo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false, unique = true)
    private Curso curso;

    @Column(columnDefinition = "text")
    private String descripcion;
}

