package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resultado_aprendizaje")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResultadoAprendizaje {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id")
    private Unidad unidad; // opcional, puede estar asociado a curso o unidad

    @Column(nullable = false, columnDefinition = "text")
    private String texto;

    @Column(columnDefinition = "text")
    private String tipo; // general | especifico
}
