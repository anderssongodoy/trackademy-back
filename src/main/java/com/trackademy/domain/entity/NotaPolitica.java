package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nota_politica")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotaPolitica {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso; // permite múltiples secciones

    @Column(columnDefinition = "text")
    private String seccion;

    @Column(columnDefinition = "text")
    private String texto;
}
